function checkNull(myvalue, mymessage){
	if(myvalue.trim() == ""){
		alert(mymessage);
		return false;
	}
	return true;
}

// ========== 若依风格交互增强 ==========

// Toast通知
function showToast(message, type = 'success', duration = 3000) {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    
    const icons = {
        success: '✓',
        error: '✕',
        warning: '⚠',
        info: 'ℹ'
    };
    
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
        <span class="toast-icon">${icons[type]}</span>
        <span class="toast-message">${message}</span>
        <span class="toast-close" onclick="this.parentElement.remove()">×</span>
    `;
    
    container.appendChild(toast);
    
    setTimeout(() => {
        toast.style.animation = 'slideOutRight 0.3s ease-in';
        setTimeout(() => toast.remove(), 300);
    }, duration);
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
            warning: '⚠️',
            danger: '⚠️',
            info: 'ℹ️',
            success: '✓'
        };
        
        const overlay = document.createElement('div');
        overlay.className = 'modal-overlay';
        overlay.innerHTML = `
            <div class="modal-dialog">
                <div class="modal-header">
                    <span class="modal-header-icon">${icons[type]}</span>
                    <h3 class="modal-title">${title}</h3>
                </div>
                <div class="modal-body">${message}</div>
                <div class="modal-footer">
                    <button class="btn-custom btn-info modal-cancel">${cancelText}</button>
                    <button class="btn-custom btn-${type === 'danger' ? 'danger' : 'primary'} modal-confirm">${confirmText}</button>
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