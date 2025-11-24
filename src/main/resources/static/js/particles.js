/**
 * 粒子特效系统
 * 淘宝风格电商平台 - 粒子动画效果
 */

(function() {
    'use strict';

    class ParticleSystem {
        constructor() {
            this.particles = [];
            this.container = null;
            this.init();
        }
        
        init() {
            // 创建粒子容器
            this.container = document.createElement('div');
            this.container.className = 'particles-container';
            document.body.appendChild(this.container);
            
            // 鼠标跟随光标
            this.initCursorGlow();
            
            // 返回顶部按钮
            this.initBackToTop();
            
            // 页面加载进度条
            this.initLoadingBar();
        }
        
        // 创建粒子爆发效果
        createBurst(x, y, count = 20, color = '#FF6600') {
            for (let i = 0; i < count; i++) {
                const particle = document.createElement('div');
                particle.className = 'particle';
                particle.style.left = x + 'px';
                particle.style.top = y + 'px';
                particle.style.background = color;
                
                const angle = (Math.PI * 2 * i) / count;
                const velocity = 50 + Math.random() * 50;
                const tx = Math.cos(angle) * velocity;
                const ty = Math.sin(angle) * velocity;
                
                particle.style.setProperty('--tx', tx + 'px');
                particle.style.setProperty('--ty', ty + 'px');
                
                this.container.appendChild(particle);
                
                setTimeout(() => {
                    particle.remove();
                }, 1000);
            }
        }
        
        // 创建礼花效果（支付成功等场景）
        createConfetti(count = 50) {
            const colors = ['#FF6600', '#FF4500', '#FFB84D', '#FF7519', '#DC143C'];
            
            for (let i = 0; i < count; i++) {
                const confetti = document.createElement('div');
                confetti.className = 'confetti';
                confetti.style.left = Math.random() * window.innerWidth + 'px';
                confetti.style.top = '-10px';
                confetti.style.background = colors[Math.floor(Math.random() * colors.length)];
                confetti.style.animationDelay = Math.random() * 0.5 + 's';
                confetti.style.animationDuration = (Math.random() * 2 + 2) + 's';
                
                this.container.appendChild(confetti);
                
                setTimeout(() => {
                    confetti.remove();
                }, 5000);
            }
        }
        
        // 鼠标跟随光标效果
        initCursorGlow() {
            const glow = document.createElement('div');
            glow.className = 'cursor-glow';
            document.body.appendChild(glow);
            
            let mouseX = 0, mouseY = 0;
            let currentX = 0, currentY = 0;
            
            document.addEventListener('mousemove', (e) => {
                mouseX = e.clientX;
                mouseY = e.clientY;
            });
            
            document.addEventListener('mousedown', () => {
                glow.classList.add('click');
            });
            
            document.addEventListener('mouseup', () => {
                glow.classList.remove('click');
            });
            
            const animate = () => {
                currentX += (mouseX - currentX) * 0.1;
                currentY += (mouseY - currentY) * 0.1;
                
                glow.style.left = currentX + 'px';
                glow.style.top = currentY + 'px';
                
                requestAnimationFrame(animate);
            };
            
            animate();
        }
        
        // 返回顶部按钮
        initBackToTop() {
            const button = document.createElement('div');
            button.className = 'back-to-top';
            button.setAttribute('title', '返回顶部');
            document.body.appendChild(button);
            
            // 监听滚动
            window.addEventListener('scroll', () => {
                if (window.pageYOffset > 300) {
                    button.classList.add('show');
                } else {
                    button.classList.remove('show');
                }
            });
            
            // 点击事件
            button.addEventListener('click', () => {
                window.scrollTo({
                    top: 0,
                    behavior: 'smooth'
                });
                
                // 添加粒子效果
                const rect = button.getBoundingClientRect();
                this.createBurst(
                    rect.left + rect.width / 2,
                    rect.top + rect.height / 2,
                    15
                );
            });
        }
        
        // 页面加载进度条
        initLoadingBar() {
            const bar = document.createElement('div');
            bar.className = 'page-loading-bar';
            document.body.appendChild(bar);
            
            // 页面加载时显示
            bar.classList.add('loading');
            bar.style.width = '100%';
            
            window.addEventListener('load', () => {
                setTimeout(() => {
                    bar.style.width = '100%';
                    setTimeout(() => {
                        bar.classList.remove('loading');
                        bar.style.width = '0';
                    }, 300);
                }, 200);
            });
        }
        
        // 商品卡片悬停粒子效果
        attachCardParticles() {
            const cards = document.querySelectorAll('.product-card');
            
            cards.forEach(card => {
                card.addEventListener('mouseenter', (e) => {
                    const rect = card.getBoundingClientRect();
                    this.createBurst(
                        rect.left + rect.width / 2,
                        rect.top + rect.height / 2,
                        8,
                        '#FFB84D'
                    );
                });
            });
        }
        
        // 数字滚动动画
        animateNumber(element, start, end, duration = 1000) {
            const range = end - start;
            const increment = range / (duration / 16);
            let current = start;
            
            const timer = setInterval(() => {
                current += increment;
                if ((increment > 0 && current >= end) || (increment < 0 && current <= end)) {
                    current = end;
                    clearInterval(timer);
                }
                element.textContent = Math.round(current);
                element.classList.add('number-scroll');
            }, 16);
        }
        
        // Toast 通知
        showToast(message, type = 'success', duration = 3000) {
            const toast = document.createElement('div');
            toast.className = `toast-notification ${type}`;
            
            const icons = {
                success: '✓',
                error: '✕',
                warning: '⚠',
                info: 'ℹ'
            };
            
            toast.innerHTML = `
                <div class="toast-icon">${icons[type] || icons.info}</div>
                <div class="toast-message">${message}</div>
            `;
            
            document.body.appendChild(toast);
            
            setTimeout(() => {
                toast.classList.add('show');
            }, 10);
            
            setTimeout(() => {
                toast.classList.remove('show');
                setTimeout(() => {
                    toast.remove();
                }, 300);
            }, duration);
        }
        
        // 购物车图标抖动
        shakeCartIcon() {
            const cartIcons = document.querySelectorAll('[href*="cart"], .cart-icon');
            cartIcons.forEach(icon => {
                icon.classList.add('cart-icon-shake');
                setTimeout(() => {
                    icon.classList.remove('cart-icon-shake');
                }, 500);
            });
        }
        
        // 骨架屏加载效果
        createSkeletonCard() {
            return `
                <div class="skeleton-card">
                    <div class="skeleton skeleton-img"></div>
                    <div class="skeleton skeleton-text"></div>
                    <div class="skeleton skeleton-text short"></div>
                </div>
            `;
        }
        
        // 显示骨架屏
        showSkeleton(container, count = 6) {
            const skeletons = [];
            for (let i = 0; i < count; i++) {
                skeletons.push(this.createSkeletonCard());
            }
            container.innerHTML = skeletons.join('');
        }
        
        // 图片懒加载
        lazyLoadImages() {
            const images = document.querySelectorAll('img[data-src]');
            
            const imageObserver = new IntersectionObserver((entries, observer) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        const img = entry.target;
                        img.src = img.dataset.src;
                        img.classList.add('lazy-img');
                        
                        img.onload = () => {
                            img.classList.add('loaded');
                        };
                        
                        observer.unobserve(img);
                    }
                });
            });
            
            images.forEach(img => imageObserver.observe(img));
        }
        
        // 3D卡片倾斜效果
        init3DTiltCards() {
            const cards = document.querySelectorAll('.product-card, .tilt-card');
            
            cards.forEach(card => {
                card.addEventListener('mousemove', (e) => {
                    const rect = card.getBoundingClientRect();
                    const x = e.clientX - rect.left;
                    const y = e.clientY - rect.top;
                    
                    const centerX = rect.width / 2;
                    const centerY = rect.height / 2;
                    
                    const rotateX = (y - centerY) / 10;
                    const rotateY = (centerX - x) / 10;
                    
                    card.style.setProperty('--rotate-x', `${rotateX}deg`);
                    card.style.setProperty('--rotate-y', `${rotateY}deg`);
                });
                
                card.addEventListener('mouseleave', () => {
                    card.style.setProperty('--rotate-x', '0deg');
                    card.style.setProperty('--rotate-y', '0deg');
                });
            });
        }
        
        // 导航栏滚动效果
        initNavbarScroll() {
            const navbar = document.querySelector('.navbar-custom');
            if (!navbar) return;
            
            let lastScroll = 0;
            
            window.addEventListener('scroll', () => {
                const currentScroll = window.pageYOffset;
                
                if (currentScroll > 100) {
                    navbar.classList.add('scrolled');
                } else {
                    navbar.classList.remove('scrolled');
                }
                
                lastScroll = currentScroll;
            });
        }
        
        // 订单状态进度条动画
        animateOrderProgress(completedSteps) {
            const progress = document.querySelector('.order-progress-line');
            if (!progress) return;
            
            const steps = document.querySelectorAll('.order-step');
            const percentage = (completedSteps / (steps.length - 1)) * 100;
            
            setTimeout(() => {
                progress.style.width = percentage + '%';
                
                steps.forEach((step, index) => {
                    if (index < completedSteps) {
                        setTimeout(() => {
                            step.classList.add('completed');
                        }, index * 200);
                    } else if (index === completedSteps) {
                        setTimeout(() => {
                            step.classList.add('active');
                        }, index * 200);
                    }
                });
            }, 100);
        }
        
        // 初始化所有效果
        initAllEffects() {
            // DOM加载完成后执行
            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', () => {
                    this.attachCardParticles();
                    this.init3DTiltCards();
                    this.lazyLoadImages();
                    this.initNavbarScroll();
                });
            } else {
                this.attachCardParticles();
                this.init3DTiltCards();
                this.lazyLoadImages();
                this.initNavbarScroll();
            }
        }
    }
    
    // 创建全局实例
    window.particleSystem = new ParticleSystem();
    
    // 自动初始化所有效果
    window.particleSystem.initAllEffects();
    
    // 导出到全局
    window.ParticleSystem = ParticleSystem;
    
    // 扩展jQuery（如果存在）
    if (typeof jQuery !== 'undefined') {
        jQuery.fn.particleBurst = function(count, color) {
            this.each(function() {
                const rect = this.getBoundingClientRect();
                window.particleSystem.createBurst(
                    rect.left + rect.width / 2,
                    rect.top + rect.height / 2,
                    count,
                    color
                );
            });
            return this;
        };
    }
    
})();
