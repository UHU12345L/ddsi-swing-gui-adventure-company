package Config;

import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    // Ya NO es final ni se crea al cargar la clase: se crea al pulsar "Entrar"
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry serviceRegistry;

    /**
     * Crea la SessionFactory con las credenciales introducidas por el usuario.
     * @param user usuario de acceso a la base de datos
     * @param pass contraseña de acceso a la base de datos
     * @return la SessionFactory creada
     * @throws Exception si no se puede conectar, con el mensaje de la causa del error
     */
    public static SessionFactory buildSessionFactory(String user, String pass) throws Exception {
        // Si ya había una SessionFactory abierta (un intento anterior), se cierra antes
        close();
        try {
            // Carga hibernate.cfg.xml y AÑADE usuario, contraseña y URL (no modifica el fichero)
            serviceRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .applySetting("hibernate.connection.username", user)
                    .applySetting("hibernate.connection.password", pass)
                    .applySetting("hibernate.connection.url", "jdbc:mariadb://172.18.1.241:3306/" + user)
                    .build();   // la BD de cada alumno se llama igual que su usuario
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
            return sessionFactory;
        } catch (Exception e) {
            // Si falla, se liberan los recursos y se lanza una excepción con la causa
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                serviceRegistry = null;
            }
            sessionFactory = null;
            throw new Exception("No se pudo conectar con la base de datos: " + mensajeCausaRaiz(e), e);
        }
    }

    /**
     * Devuelve el mensaje que mejor explica el error: el del driver de MariaDB
     * (SQLException) si lo hay; si no, el último mensaje de la cadena de causas.
     */
    private static String mensajeCausaRaiz(Throwable e) {
        String mensaje = e.getMessage();
        Throwable causa = e;
        while (causa != null) {                  // recorre la excepción y sus causas
            if (causa instanceof SQLException) { // instanceof: ¿es de tipo SQLException?
                return causa.getMessage();
            }
            if (causa.getMessage() != null) {
                mensaje = causa.getMessage();
            }
            causa = causa.getCause();            // pasa a la causa de esta excepción
        }
        return mensaje;
    }

    /**
     * Cierra la SessionFactory y libera los recursos de Hibernate.
     */
    public static void close() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } finally {
            sessionFactory = null;
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                serviceRegistry = null;
            }
        }
    }
}