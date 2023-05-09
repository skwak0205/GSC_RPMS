define("DS/ENOExportV5WebCommands/ENOExportV5WebCommands_ru",{});define("DS/ENOExportV5WebCommands/assets/nls/ExportV5Cmd",{title:"Экспорт V5",init:"инициализировано! ",loading:"Загрузка объектов для экспорта",option_path:{label:"Экспорт в каталог",option_text:"Введите здесь путь",option_combo:"Выбрать расположение ...",button_add:"Добавить",predefined_paths:"Предопределенные пути",added_path:"Локальное определение"},option_conflict:{option_overwrite:"Перезаписать",option_copy:"Сохранить копию существующего файла в случае конфликта на диске"},generatingfilename:"Создание имен файлов...",buttonLaunch:"Запуск",buttonExport:"Экспорт",buttonCancel:"Отмена",error:{title:"Ошибка",noSelection:"Выберите объект для экспорта перед запуском команды Экспорт V5 ",empty:"Экспорт невозможен: определенное правило создало пустое имя файла.",unicity:{BL:"Экспорт невозможен: определенное правило не создало уникальных имен файлов {ofileName}.",noBL:"Экспорт невозможен: созданные имена файлов, основанные на использовании заголовка, не сгенерировали уникальных имен файлов {ofileName}. Определите правило именования."},expand_empty:"Экспорт невозможен: результат развертывания пуст.",expand_failed_title:"Сбой экспорта",type:"Экспорт невозможен: объект {title} с типом {objType} запрещен для экспорта. Отмените выбор объекта перед выполнением команды Экспорт V5.",CADMaster:"Экспорт невозможен: объект {title} с источником {cadmaster} CAD запрещен для экспорта. Отмените выбор объекта перед выполнением команды Экспорт V5.",launchApp:"Сбой при запуске пакета.",errorType:"ИД ошибки: {errorID}",fetchFailed:"Ошибка при извлечении информации из объекта.",emptyDefinedPath:"Список разрешенных путей пуст. Обратитесь к администратору.",pathInvalid:"Путь, определенный именами {namesList}, является недопустимым. Обратитесь к администратору.",emptyPathIdentifier:"Определенные пути должны содержать имя. Обратитесь к администратору.",identifierUnicity:"Следующие имена не являются уникальными: {namesList}. Определенные пути должны содержать уникальное имя. Обратитесь к администратору",emptyobject:"Неверный синтаксис объявления пути. Обратитесь к администратору",timeout:"Время ожидания запроса истекло через {seconds} мс.",security_context_fail:"Запрос сервера не может быть отправлен: сбой при получении контекста безопасности.",expandIssue:"Некоторые данные могут быть еще не проиндексированы. Подождите несколько минут, прежде чем предпринимать новую попытку.",code401:"Ошибка 401. Проблема аутентификации",code400:"Ошибка 400. Неверный запрос",genericError:"Ошибка при обработке запроса"},warning:{title:"Предупреждение",title_path:"Недопустимый путь.",empty_path:"Невозможно выполнить экспорт в пустое расположение",invalid_select:"Некоторые выбранные файлы не поддерживаются",invalid_path:"выбранный путь является недопустимым. Исправьте или введите новый.",invalid_fetchanswer:"Некоторые вновь созданные объекты еще не проиндексированы и не могут быть экспортированы."},info:{title:"Информация",message:"Сообщение отправлено."},success:{title:"Успешно",message:"Данные отправлены в CATIA V5 для экспорта."},processing:"Создание имен файлов",launch_batch:"Запуск пакета...","ExportV5.Msg.InvalidInput":"Экспорт в CATIA V5 одних или нескольких выбранных данных не поддерживается.","ExportV5.Msg.ExecutionFailed":"Непредвиденная ошибка.","ExportV5.Msg.InvalidRole":"У вас нет роли для использования экспорта в CATIA V5","ExportV5.Msg.InvalidInputCADMaster":"Одни или несколько выбранных данных имеют неподдерживаемый главный элемент CAD. Экспорт в CATIA V5 не поддерживается.","ExportV5.Msg.ExpandResultEmpty":"Результат развертывания выбранных данных пуст. Экспорт в CATIA V5 невозможен.","ExportV5.Msg.ExecutionFailed.Subtitle":"Contact your administrator for support."});