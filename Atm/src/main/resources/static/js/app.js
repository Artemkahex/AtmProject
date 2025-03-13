// Ожидаем полной загрузки DOM
document.addEventListener("DOMContentLoaded", function() {
    console.log("ATM проект загружен!");

    // Добавляем классы анимации к основным элементам
    addAnimationClasses();

    // Анимация перехода между страницами
    setupPageTransitions();

    // Анимация кнопок и интерактивных элементов
    setupButtonEffects();

    // Анимация появления сообщений об ошибках и успешных операциях
    animateMessages();
});

// Добавление классов анимации к элементам страницы
function addAnimationClasses() {
    // Анимация заголовка
    const mainHeader = document.querySelector('h1');
    if (mainHeader) {
        mainHeader.classList.add('fade-in');
    }

    // Анимация форм
    const forms = document.querySelectorAll('form');
    forms.forEach((form, index) => {
        form.style.animationDelay = `${index * 0.1}s`;
        form.classList.add('fade-in');
    });

    // Анимация карточек счетов
    const accountItems = document.querySelectorAll('.account-item');
    accountItems.forEach((item, index) => {
        item.style.animationDelay = `${index * 0.1}s`;
        item.classList.add('slide-in');
    });

    // Анимация элементов навигации
    const navLinks = document.querySelectorAll('nav a');
    navLinks.forEach((link, index) => {
        link.style.animationDelay = `${index * 0.1}s`;
        link.classList.add('fade-in');
    });

    // Анимация таблицы транзакций
    const transactionTable = document.querySelector('.transactions-table');
    if (transactionTable) {
        transactionTable.classList.add('fade-in');
    }
}

// Настройка эффектов для кнопок и интерактивных элементов
function setupButtonEffects() {
    const buttons = document.querySelectorAll('button');
    buttons.forEach(button => {
        button.addEventListener('mousedown', function() {
            this.style.transform = 'scale(0.98)';
        });

        button.addEventListener('mouseup', function() {
            this.style.transform = '';
        });

        button.addEventListener('mouseleave', function() {
            this.style.transform = '';
        });
    });
}

// Анимация переходов между страницами
function setupPageTransitions() {
    const links = document.querySelectorAll('a:not([target="_blank"])');

    links.forEach(link => {
        link.addEventListener('click', function(e) {
            // Проверяем, что это не внешняя ссылка
            if (this.hostname === window.location.hostname) {
                e.preventDefault();
                const href = this.getAttribute('href');

                // Анимация исчезновения текущей страницы
                document.body.style.opacity = 0;
                document.body.style.transition = 'opacity 0.3s ease';

                // Переход на новую страницу после завершения анимации
                setTimeout(() => {
                    window.location.href = href;
                }, 300);
            }
        });
    });

    // Анимация появления страницы при загрузке
    window.addEventListener('pageshow', function() {
        document.body.style.opacity = 0;
        setTimeout(() => {
            document.body.style.opacity = 1;
            document.body.style.transition = 'opacity 0.3s ease';
        }, 10);
    });
}

// Анимация сообщений об ошибках и успешных операциях
function animateMessages() {
    const errorMessage = document.querySelector('.error-message');
    const successMessage = document.querySelector('.success-message');

    // Добавляем класс анимации к сообщениям
    if (errorMessage) {
        errorMessage.classList.add('fade-in');

        // Создаем кнопку закрытия для сообщения об ошибке
        addCloseButton(errorMessage);
    }

    if (successMessage) {
        successMessage.classList.add('fade-in');

        // Автоматически скрываем сообщение об успешной операции через 5 секунд
        setTimeout(() => {
            if (successMessage.parentNode) {
                fadeOut(successMessage);
            }
        }, 5000);

        // Создаем кнопку закрытия для сообщения об успешной операции
        addCloseButton(successMessage);
    }
}

// Добавление кнопки закрытия к сообщению
function addCloseButton(messageElement) {
    const closeButton = document.createElement('button');
    closeButton.innerHTML = '&times;';
    closeButton.style.position = 'absolute';
    closeButton.style.right = '10px';
    closeButton.style.top = '10px';
    closeButton.style.backgroundColor = 'transparent';
    closeButton.style.border = 'none';
    closeButton.style.fontSize = '20px';
    closeButton.style.cursor = 'pointer';
    closeButton.style.color = 'inherit';
    closeButton.style.width = 'auto';
    closeButton.style.padding = '0';
    closeButton.style.lineHeight = '1';
    closeButton.style.boxShadow = 'none';

    closeButton.addEventListener('click', function() {
        fadeOut(messageElement);
    });

    // Устанавливаем позицию relative для сообщения, если не установлена
    messageElement.style.position = 'relative';

    // Добавляем кнопку закрытия в сообщение
    messageElement.appendChild(closeButton);
}

// Функция для плавного скрытия элемента
function fadeOut(element) {
    element.style.opacity = '1';
    element.style.transition = 'opacity 0.5s ease';

    setTimeout(() => {
        element.style.opacity = '0';
    }, 10);

    setTimeout(() => {
        if (element.parentNode) {
            element.parentNode.removeChild(element);
        }
    }, 500);
}

// Добавление эффектов для полей ввода
document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('input');

    inputs.forEach(input => {
        // Добавляем класс активного поля при фокусе
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('active-input');
        });

        // Удаляем класс активного поля при потере фокуса
        input.addEventListener('blur', function() {
            this.parentElement.classList.remove('active-input');
        });
    });
});