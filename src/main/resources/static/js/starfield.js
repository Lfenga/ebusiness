/**
 * 星空背景特效系统
 * 淘宝风格电商平台 - 星空动画效果
 */

(function() {
    'use strict';

    class StarField {
        constructor(options = {}) {
            this.canvas = null;
            this.ctx = null;
            this.stars = [];
            this.shootingStars = [];
            
            // 配置参数
            this.config = {
                starCount: options.starCount || 200,
                starColor: options.starColor || '#FFFFFF',
                starSize: options.starSize || { min: 1, max: 3 },
                twinkleSpeed: options.twinkleSpeed || 0.02,
                moveSpeed: options.moveSpeed || 0.5,
                shootingStarInterval: options.shootingStarInterval || 3000,
                enableShootingStars: options.enableShootingStars !== false,
                enableTwinkle: options.enableTwinkle !== false,
                bgGradient: options.bgGradient || {
                    start: '#0a0a0a',
                    end: '#1a1a1a'
                }
            };
            
            this.init();
        }
        
        init() {
            // 创建Canvas元素
            this.canvas = document.createElement('canvas');
            this.canvas.id = 'starfield-canvas';
            this.canvas.style.position = 'fixed';
            this.canvas.style.top = '0';
            this.canvas.style.left = '0';
            this.canvas.style.width = '100%';
            this.canvas.style.height = '100%';
            this.canvas.style.zIndex = '-1';
            this.canvas.style.pointerEvents = 'none';
            
            // 插入到body开头
            if (document.body.firstChild) {
                document.body.insertBefore(this.canvas, document.body.firstChild);
            } else {
                document.body.appendChild(this.canvas);
            }
            
            this.ctx = this.canvas.getContext('2d');
            this.resizeCanvas();
            
            // 初始化星星
            this.initStars();
            
            // 事件监听
            window.addEventListener('resize', () => this.resizeCanvas());
            
            // 启动流星
            if (this.config.enableShootingStars) {
                this.startShootingStars();
            }
            
            // 开始动画
            this.animate();
        }
        
        resizeCanvas() {
            this.canvas.width = window.innerWidth;
            this.canvas.height = window.innerHeight;
        }
        
        initStars() {
            this.stars = [];
            for (let i = 0; i < this.config.starCount; i++) {
                this.stars.push(this.createStar());
            }
        }
        
        createStar() {
            const size = Math.random() * (this.config.starSize.max - this.config.starSize.min) + this.config.starSize.min;
            return {
                x: Math.random() * this.canvas.width,
                y: Math.random() * this.canvas.height,
                size: size,
                baseSize: size,
                opacity: Math.random(),
                twinkleSpeed: Math.random() * this.config.twinkleSpeed + 0.01,
                dx: (Math.random() - 0.5) * this.config.moveSpeed,
                dy: (Math.random() - 0.5) * this.config.moveSpeed
            };
        }
        
        createShootingStar() {
            const startX = Math.random() * this.canvas.width;
            const startY = Math.random() * this.canvas.height * 0.5;
            const angle = Math.PI / 4 + (Math.random() - 0.5) * Math.PI / 6;
            
            return {
                x: startX,
                y: startY,
                length: Math.random() * 80 + 40,
                speed: Math.random() * 5 + 3,
                angle: angle,
                opacity: 1,
                decay: 0.015 + Math.random() * 0.01
            };
        }
        
        startShootingStars() {
            setInterval(() => {
                if (Math.random() > 0.7) {
                    this.shootingStars.push(this.createShootingStar());
                }
            }, this.config.shootingStarInterval);
        }
        
        drawStar(star) {
            this.ctx.beginPath();
            
            // 创建星星的辉光效果
            const gradient = this.ctx.createRadialGradient(
                star.x, star.y, 0,
                star.x, star.y, star.size * 2
            );
            gradient.addColorStop(0, `rgba(255, 255, 255, ${star.opacity})`);
            gradient.addColorStop(0.5, `rgba(255, 200, 100, ${star.opacity * 0.5})`);
            gradient.addColorStop(1, `rgba(255, 102, 0, 0)`);
            
            this.ctx.fillStyle = gradient;
            this.ctx.arc(star.x, star.y, star.size * 2, 0, Math.PI * 2);
            this.ctx.fill();
            
            // 核心星点
            this.ctx.beginPath();
            this.ctx.fillStyle = `rgba(255, 255, 255, ${star.opacity})`;
            this.ctx.arc(star.x, star.y, star.size, 0, Math.PI * 2);
            this.ctx.fill();
        }
        
        drawShootingStar(star) {
            const dx = Math.cos(star.angle) * star.length;
            const dy = Math.sin(star.angle) * star.length;
            
            // 绘制拖尾
            const gradient = this.ctx.createLinearGradient(
                star.x, star.y,
                star.x - dx, star.y - dy
            );
            gradient.addColorStop(0, `rgba(255, 200, 100, ${star.opacity})`);
            gradient.addColorStop(0.5, `rgba(255, 150, 50, ${star.opacity * 0.5})`);
            gradient.addColorStop(1, `rgba(255, 102, 0, 0)`);
            
            this.ctx.strokeStyle = gradient;
            this.ctx.lineWidth = 2;
            this.ctx.beginPath();
            this.ctx.moveTo(star.x, star.y);
            this.ctx.lineTo(star.x - dx, star.y - dy);
            this.ctx.stroke();
            
            // 绘制头部亮点
            this.ctx.beginPath();
            this.ctx.fillStyle = `rgba(255, 255, 255, ${star.opacity})`;
            this.ctx.arc(star.x, star.y, 3, 0, Math.PI * 2);
            this.ctx.fill();
        }
        
        updateStar(star) {
            // 闪烁效果
            if (this.config.enableTwinkle) {
                star.opacity += star.twinkleSpeed;
                if (star.opacity > 1 || star.opacity < 0.2) {
                    star.twinkleSpeed = -star.twinkleSpeed;
                }
            }
            
            // 移动
            star.x += star.dx;
            star.y += star.dy;
            
            // 边界检测
            if (star.x < 0) star.x = this.canvas.width;
            if (star.x > this.canvas.width) star.x = 0;
            if (star.y < 0) star.y = this.canvas.height;
            if (star.y > this.canvas.height) star.y = 0;
        }
        
        updateShootingStar(star) {
            star.x += Math.cos(star.angle) * star.speed;
            star.y += Math.sin(star.angle) * star.speed;
            star.opacity -= star.decay;
        }
        
        drawBackground() {
            // 绘制渐变背景
            const gradient = this.ctx.createLinearGradient(0, 0, 0, this.canvas.height);
            gradient.addColorStop(0, this.config.bgGradient.start);
            gradient.addColorStop(1, this.config.bgGradient.end);
            
            this.ctx.fillStyle = gradient;
            this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
        }
        
        animate() {
            // 清空画布并绘制背景
            this.drawBackground();
            
            // 绘制和更新普通星星
            this.stars.forEach(star => {
                this.updateStar(star);
                this.drawStar(star);
            });
            
            // 绘制和更新流星
            this.shootingStars = this.shootingStars.filter(star => {
                this.updateShootingStar(star);
                if (star.opacity > 0) {
                    this.drawShootingStar(star);
                    return true;
                }
                return false;
            });
            
            requestAnimationFrame(() => this.animate());
        }
        
        // 性能优化：根据设备性能调整星星数量
        adjustPerformance() {
            const fps = this.measureFPS();
            if (fps < 30 && this.config.starCount > 50) {
                this.config.starCount = Math.max(50, this.config.starCount - 50);
                this.initStars();
            }
        }
        
        measureFPS() {
            // 简单的FPS测量
            return 60; // 默认返回60，实际应用中可以实现真实FPS测量
        }
        
        destroy() {
            if (this.canvas && this.canvas.parentNode) {
                this.canvas.parentNode.removeChild(this.canvas);
            }
            window.removeEventListener('resize', this.resizeCanvas);
        }
    }
    
    // 自动初始化
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            window.starField = new StarField({
                starCount: 150,
                enableShootingStars: true,
                enableTwinkle: true,
                shootingStarInterval: 4000,
                bgGradient: {
                    start: '#0a0a0a',
                    end: '#1a1a2a'
                }
            });
        });
    } else {
        window.starField = new StarField({
            starCount: 150,
            enableShootingStars: true,
            enableTwinkle: true,
            shootingStarInterval: 4000,
            bgGradient: {
                start: '#0a0a0a',
                end: '#1a1a2a'
            }
        });
    }
    
    // 导出到全局
    window.StarField = StarField;
    
})();
