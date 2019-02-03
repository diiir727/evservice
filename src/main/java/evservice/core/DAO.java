package evservice.core;

/**
 * Интерфейс для работы с бд
 */
public interface DAO {

    /**
     * Регистрация пользователя
     * @param user пользователь
     * @return true если пользователь зарегестрирован, иначе false
     * @throws DAOException выбрасывается при возникновении исключительных ситуациях в бд
     */
    boolean registerUser(User user) throws DAOException;

    /**
     * Получение баланса пользователя
     * @param user пользователь
     * @return сумма всех транзакций пользователя
     * @throws DAOException выбрасывается при возникновении исключительных ситуациях в бд
     */
    double getBalance(User user) throws DAOException;

    /**
     * Создание транзакции
     * @param transaction транзакция
     * @throws DAOException выбрасывается при возникновении исключительных ситуациях в бд
     */
    void createTransaction(UserTransaction transaction) throws DAOException;

    /**
     * Получение пользователя по логину
     * @param login логин пользователя
     * @return User если он существует, иначе null
     * @throws DAOException выбрасывается при возникновении исключительных ситуациях в бд
     */
    User getUserByLogin(String login) throws DAOException;

}
