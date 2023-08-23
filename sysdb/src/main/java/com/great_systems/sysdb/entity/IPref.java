package com.great_systems.sysdb.entity;

public interface IPref {


    String NEED_REGISTRATION_PREF_KEY = "com.great_systems.abacus.need_registration_key"; // запросить регистрацию
    String NEED_LOGIN_PREF_KEY = "com.great_systems.abacus.need_login_key"; // запросить логин
    String NEED_AUTOLOGIN_PREF_KEY = "com.great_systems.abacus.auto_login_key"; // перелогинится автоматически
    String NEED_SESSION_PREF_KEY = "com.great_systems.abacus.need_session_key";  // текущая сессия
    String LOGIN_SESSION_PREF_KEY = "com.great_systems.abacus.login_session_key";  // логин пользователя, под которым был вход
    String PASSWORD_SESSION_PREF_KEY = "com.great_systems.abacus.password_session_key"; // пароль пользователя, под которым был вход
    String DATA_SERVER_URL_PREF_KEY = "com.great_systems.abacus.data_server_url_key"; // сервер данных для пользователя
    String DATA_USER_PREF_KEY = "com.great_systems.abacus.data_user_key"; // сервер данных для пользователя
    String DATA_COMPANY_PREF_KEY = "com.great_systems.abacus.data_user_key"; // сервер данных для пользователя


    String TASK_SIZE_PREF_KEY = "com.great_systems.abacus.task_pack_size_key"; // максимальное количество задач в пакете
    String COMMAND_PREF_KEY = "com.great_systems.abacus.command_key"; // последняя обработанная команда

    String NAV_COMMAND = "com.great_systems.abacus.NAV_COMMAND_KEY";

    int NAV_COMMAND_CHECK_SESSION = 101;
    int NAV_COMMAND_GET_REGISTRATION = 102;
    int NAV_COMMAND_GET_LOGIN = 103;

}
