package io.diplom.exception

class LoginException : GeneralException("Неверный логин", 403)
class PasswordException : GeneralException("Неверный пароль", 403)
