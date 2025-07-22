let notificationCount = 0;

function showNotification(type, title, message, duration = 5000) {
    notificationCount++;

    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.id = `notification-${notificationCount}`;

    const icons = {
        error: `
        <svg viewBox="0 0 24 24" width="20" height="20" fill="white">
            <path d="M12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.6 0 12 0zm1 17h-2v-2h2v2zm0-4h-2V7h2v6z"/>
        </svg>`,
        success: `
        <svg viewBox="0 0 24 24" width="20" height="20" fill="white">
            <path d="M12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.6 0 12 0zm-2 17l-5-5 1.4-1.4L10 14.2l7.6-7.6L19 8l-9 9z"/>
        </svg>`,
        warning: `
        <svg viewBox="0 0 24 24" width="20" height="20" fill="white">
            <path d="M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z"/>
        </svg>`,
        info: `
        <svg viewBox="0 0 24 24" width="20" height="20" fill="white">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 15h-1v-6h2v6h-1zm0-8h-1V7h2v2h-1z"/>
        </svg>`
    };

    notification.innerHTML = `
        <div class="notification-icon">${icons[type]}</div>
        <div class="notification-content">
            <div class="notification-title">${title}</div>
            <div class="notification-message">${message}</div>
        </div>
        <button class="notification-close" onclick="hideNotification('${notification.id}')">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="white">
                <path d="M18.3 5.71a1 1 0 0 0-1.41 0L12 10.59 7.11 5.7A1 1 0 1 0 5.7 7.11L10.59 12l-4.89 4.89a1 1 0 1 0 1.41 1.41L12 13.41l4.89 4.89a1 1 0 0 0 1.41-1.41L13.41 12l4.89-4.89a1 1 0 0 0 0-1.4z"/>
            </svg>
        </button>
        <div class="notification-progress"></div>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.classList.add('show');
    }, 100);

    setTimeout(() => {
        hideNotification(notification.id);
    }, duration);
}

function hideNotification(id) {
    const notification = document.getElementById(id);
    if (notification) {
        notification.classList.add('hide');
        setTimeout(() => {
            notification.remove();
        }, 500);
    }
}




