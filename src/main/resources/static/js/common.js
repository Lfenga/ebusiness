function checkNull(myvalue, mymessage){
	if(myvalue.trim() == ""){
		showAlert(mymessage, 'warning');
		return false;
	}
	return true;
}

// Element Plus 风格的 Alert 消息框
function showAlert(message, type = 'info', duration = 4000) {
    const container = getOrCreateAlertContainer();
    
    const icons = {
        success: '<svg class="alert-icon" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm-55.808 536.384-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z" fill="currentColor"/></svg>',
        warning: '<svg class="alert-icon" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm0 192a58.432 58.432 0 0 0-58.24 63.744l23.36 256.384a35.072 35.072 0 0 0 69.76 0l23.296-256.384A58.432 58.432 0 0 0 512 256zm0 512a51.2 51.2 0 1 0 0-102.4 51.2 51.2 0 0 0 0 102.4z" fill="currentColor"/></svg>',
        error: '<svg class="alert-icon" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm0 393.664L407.936 353.6a38.4 38.4 0 1 0-54.336 54.336L457.664 512 353.6 616.064a38.4 38.4 0 1 0 54.336 54.336L512 566.336 616.064 670.4a38.4 38.4 0 1 0 54.336-54.336L566.336 512 670.4 407.936a38.4 38.4 0 1 0-54.336-54.336L512 457.664z" fill="currentColor"/></svg>',
        info: '<svg class="alert-icon" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896.064A448 448 0 0 1 512 64zm23.744 191.488c0-15.616-10.624-23.04-31.744-23.04s-31.808 7.424-31.808 23.04v85.44c0 15.488 10.688 23.36 31.808 23.36s31.744-7.872 31.744-23.36v-85.44zm4.8 253.44H486.72c-15.488 0-23.04 10.688-23.04 31.744v201.216c0 15.552 10.752 23.744 32.256 23.744h22.208c15.552 0 23.296-8.192 23.296-23.744V540.672c0-21.056-7.744-31.744-23.296-31.744z" fill="currentColor"/></svg>'
    };
    
    const alert = document.createElement('div');
    alert.className = `el-alert el-alert--${type}`;
    alert.innerHTML = `
        <div class="el-alert__content">
            ${icons[type]}
            <span class="el-alert__title">${message}</span>
            <button class="el-alert__close-btn" onclick="this.closest('.el-alert').remove()">
                <svg viewBox="0 0 1024 1024"><path d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z" fill="currentColor"/></svg>
            </button>
        </div>
    `;
    
    container.appendChild(alert);
    
    // 入场动画
    setTimeout(() => alert.classList.add('el-alert--show'), 10);
    
    // 自动关闭
    if (duration > 0) {
        setTimeout(() => {
            alert.classList.add('el-alert--leave');
            setTimeout(() => alert.remove(), 300);
        }, duration);
    }
}

function getOrCreateAlertContainer() {
    let container = document.querySelector('.el-alert-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'el-alert-container';
        document.body.appendChild(container);
    }
    return container;
}

// ========== 若依风格交互增强 ==========

// Element Plus 风格的 Message 通知
function showToast(message, type = 'success', duration = 3000) {
    let container = document.querySelector('.el-message-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'el-message-container';
        document.body.appendChild(container);
    }
    
    const icons = {
        success: '<svg class="el-message__icon" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm-55.808 536.384-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z" fill="currentColor"/></svg>',
        error: '<svg class="el-message__icon" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm0 393.664L407.936 353.6a38.4 38.4 0 1 0-54.336 54.336L457.664 512 353.6 616.064a38.4 38.4 0 1 0 54.336 54.336L512 566.336 616.064 670.4a38.4 38.4 0 1 0 54.336-54.336L566.336 512 670.4 407.936a38.4 38.4 0 1 0-54.336-54.336L512 457.664z" fill="currentColor"/></svg>',
        warning: '<svg class="el-message__icon" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm0 192a58.432 58.432 0 0 0-58.24 63.744l23.36 256.384a35.072 35.072 0 0 0 69.76 0l23.296-256.384A58.432 58.432 0 0 0 512 256zm0 512a51.2 51.2 0 1 0 0-102.4 51.2 51.2 0 0 0 0 102.4z" fill="currentColor"/></svg>',
        info: '<svg class="el-message__icon" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896.064A448 448 0 0 1 512 64zm23.744 191.488c0-15.616-10.624-23.04-31.744-23.04s-31.808 7.424-31.808 23.04v85.44c0 15.488 10.688 23.36 31.808 23.36s31.744-7.872 31.744-23.36v-85.44zm4.8 253.44H486.72c-15.488 0-23.04 10.688-23.04 31.744v201.216c0 15.552 10.752 23.744 32.256 23.744h22.208c15.552 0 23.296-8.192 23.296-23.744V540.672c0-21.056-7.744-31.744-23.296-31.744z" fill="currentColor"/></svg>'
    };
    
    const toast = document.createElement('div');
    toast.className = `el-message el-message--${type}`;
    toast.innerHTML = `
        <div class="el-message__content">
            ${icons[type]}
            <span class="el-message__text">${message}</span>
            <button class="el-message__close" onclick="this.closest('.el-message').remove()">
                <svg viewBox="0 0 1024 1024"><path d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z" fill="currentColor"/></svg>
            </button>
        </div>
    `;
    
    container.appendChild(toast);
    
    // 入场动画
    setTimeout(() => toast.classList.add('el-message--show'), 10);
    
    // 自动关闭
    if (duration > 0) {
        setTimeout(() => {
            toast.classList.add('el-message--leave');
            setTimeout(() => toast.remove(), 300);
        }, duration);
    }
}

// 确认对话框
function showConfirm(options) {
    return new Promise((resolve) => {
        const {
            title = '提示',
            message = '确定要执行此操作吗？',
            confirmText = '确定',
            cancelText = '取消',
            type = 'warning'
        } = options;
        
        const icons = {
            warning: '<svg class="el-messagebox__status" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm0 192a58.432 58.432 0 0 0-58.24 63.744l23.36 256.384a35.072 35.072 0 0 0 69.76 0l23.296-256.384A58.432 58.432 0 0 0 512 256zm0 512a51.2 51.2 0 1 0 0-102.4 51.2 51.2 0 0 0 0 102.4z" fill="#E6A23C"/></svg>',
            danger: '<svg class="el-messagebox__status" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm0 393.664L407.936 353.6a38.4 38.4 0 1 0-54.336 54.336L457.664 512 353.6 616.064a38.4 38.4 0 1 0 54.336 54.336L512 566.336 616.064 670.4a38.4 38.4 0 1 0 54.336-54.336L566.336 512 670.4 407.936a38.4 38.4 0 1 0-54.336-54.336L512 457.664z" fill="#F56C6C"/></svg>',
            info: '<svg class="el-messagebox__status" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896.064A448 448 0 0 1 512 64zm23.744 191.488c0-15.616-10.624-23.04-31.744-23.04s-31.808 7.424-31.808 23.04v85.44c0 15.488 10.688 23.36 31.808 23.36s31.744-7.872 31.744-23.36v-85.44zm4.8 253.44H486.72c-15.488 0-23.04 10.688-23.04 31.744v201.216c0 15.552 10.752 23.744 32.256 23.744h22.208c15.552 0 23.296-8.192 23.296-23.744V540.672c0-21.056-7.744-31.744-23.296-31.744z" fill="#909399"/></svg>',
            success: '<svg class="el-messagebox__status" viewBox="0 0 1024 1024"><path d="M512 64a448 448 0 1 1 0 896 448 448 0 0 1 0-896zm-55.808 536.384-99.52-99.584a38.4 38.4 0 1 0-54.336 54.336l126.72 126.72a38.272 38.272 0 0 0 54.336 0l262.4-262.464a38.4 38.4 0 1 0-54.272-54.336L456.192 600.384z" fill="#67C23A"/></svg>'
        };
        
        const overlay = document.createElement('div');
        overlay.className = 'el-overlay el-overlay--show';
        overlay.innerHTML = `
            <div class="el-messagebox el-messagebox--show">
                <div class="el-messagebox__header">
                    <div class="el-messagebox__title">${title}</div>
                    <button class="el-messagebox__headerbtn" onclick="this.closest('.el-overlay').remove()">
                        <svg class="el-icon el-icon-close" viewBox="0 0 1024 1024"><path d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z" fill="currentColor"/></svg>
                    </button>
                </div>
                <div class="el-messagebox__content">
                    <div class="el-messagebox__container">
                        ${icons[type]}
                        <div class="el-messagebox__message">${message}</div>
                    </div>
                </div>
                <div class="el-messagebox__btns">
                    <button class="el-button el-button--default el-button--small modal-cancel">${cancelText}</button>
                    <button class="el-button el-button--primary el-button--small modal-confirm">${confirmText}</button>
                </div>
            </div>
        `;
        
        document.body.appendChild(overlay);
        
        overlay.querySelector('.modal-confirm').onclick = () => {
            overlay.remove();
            resolve(true);
        };
        
        overlay.querySelector('.modal-cancel').onclick = () => {
            overlay.remove();
            resolve(false);
        };
        
        overlay.onclick = (e) => {
            if (e.target === overlay) {
                overlay.remove();
                resolve(false);
            }
        };
    });
}

// 图片预览
function previewImage(src) {
    const overlay = document.createElement('div');
    overlay.className = 'image-preview-overlay';
    overlay.innerHTML = `<img src="${src}" class="image-preview-large">`;
    overlay.onclick = () => overlay.remove();
    document.body.appendChild(overlay);
}

// 侧边栏菜单切换
function initSidebarMenu() {
    document.querySelectorAll('.sidebar-menu-link').forEach(link => {
        if (link.nextElementSibling && link.nextElementSibling.classList.contains('sidebar-submenu')) {
            link.onclick = function(e) {
                e.preventDefault();
                const parent = this.parentElement;
                const isOpen = parent.classList.contains('open');
                
                // 关闭其他菜单
                document.querySelectorAll('.sidebar-menu-item.open').forEach(item => {
                    if (item !== parent) {
                        item.classList.remove('open');
                    }
                });
                
                // 切换当前菜单
                parent.classList.toggle('open', !isOpen);
            };
        }
    });
    
    // 设置当前激活菜单
    const currentPath = window.location.pathname;
    document.querySelectorAll('.sidebar-submenu-link').forEach(link => {
        if (link.getAttribute('href') && currentPath.includes(link.getAttribute('href'))) {
            link.classList.add('active');
            const parent = link.closest('.sidebar-menu-item');
            if (parent) {
                parent.classList.add('open');
            }
        }
    });
}

// 表格行悬停效果（已在CSS中实现，此处为兼容性增强）
function enhanceTableHover() {
    document.querySelectorAll('.custom-table tbody tr').forEach(row => {
        row.addEventListener('mouseenter', function() {
            this.style.backgroundColor = '#f5f7fa';
        });
        row.addEventListener('mouseleave', function() {
            this.style.backgroundColor = '';
        });
    });
}

// 加载动画
function showLoading() {
    if (!document.querySelector('.loading-overlay')) {
        const loading = document.createElement('div');
        loading.className = 'loading-overlay';
        loading.innerHTML = '<div class="loading-spinner"></div>';
        document.body.appendChild(loading);
    }
}

function hideLoading() {
    const loading = document.querySelector('.loading-overlay');
    if (loading) {
        loading.remove();
    }
}

// 表单验证增强
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return true;
    
    let isValid = true;
    form.querySelectorAll('[required]').forEach(field => {
        if (!field.value.trim()) {
            field.style.borderColor = '#f56c6c';
            isValid = false;
            
            field.addEventListener('input', function() {
                this.style.borderColor = '';
            }, { once: true });
        }
    });
    
    if (!isValid) {
        showToast('请填写所有必填项', 'error');
    }
    
    return isValid;
}

// 文件上传预览
function handleFilePreview(input, previewId) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const preview = document.getElementById(previewId);
            if (preview) {
                if (preview.tagName === 'IMG') {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                } else {
                    preview.innerHTML = `<img src="${e.target.result}" class="image-preview" onclick="previewImage('${e.target.result}')">`;
                }
            }
        };
        reader.readAsDataURL(input.files[0]);
    }
}

// 删除操作增强
async function confirmDelete(id, url, message = '确定要删除此项吗？') {
    const confirmed = await showConfirm({
        title: '删除确认',
        message: message,
        type: 'danger',
        confirmText: '删除',
        cancelText: '取消'
    });
    
    if (confirmed) {
        showLoading();
        $.ajax({
            url: url,
            type: 'POST',
            data: { id: id },
            success: function(result) {
                hideLoading();
                if (result === 'yes' || result.includes('selectAllGoodsByPage') || result.includes('selectAllTypeByPage')) {
                    showToast('删除成功', 'success');
                    setTimeout(() => {
                        window.location.reload();
                    }, 1000);
                } else if (result === 'no') {
                    showToast('该项有关联数据，无法删除', 'error');
                } else {
                    showToast('删除失败', 'error');
                }
            },
            error: function() {
                hideLoading();
                showToast('操作失败，请重试', 'error');
            }
        });
    }
}

// 页面初始化
document.addEventListener('DOMContentLoaded', function() {
    // 初始化侧边栏菜单
    initSidebarMenu();
    
    // 初始化表格悬停效果
    enhanceTableHover();
    
    // 为所有图片添加预览功能
    document.querySelectorAll('img.preview-enabled').forEach(img => {
        img.style.cursor = 'pointer';
        img.onclick = function() {
            previewImage(this.src);
        };
    });
    
    // 表单提交前验证
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredFields = this.querySelectorAll('[required]');
            let hasEmpty = false;
            
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    field.style.borderColor = '#f56c6c';
                    hasEmpty = true;
                }
            });
            
            if (hasEmpty) {
                e.preventDefault();
                showToast('请填写所有必填项', 'error');
            }
        });
    });
});