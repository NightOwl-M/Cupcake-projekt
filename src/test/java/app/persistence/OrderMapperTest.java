package app.persistence;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
class OrderMapperTest {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=test";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    @BeforeAll
    public static void setup() {
        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement()) {


            stmt.execute("ALTER TABLE IF EXISTS public.orders DROP CONSTRAINT IF EXISTS orders_user_id_fkey;");
            stmt.execute("ALTER TABLE IF EXISTS public.productline DROP CONSTRAINT IF EXISTS productline_bottom_id_fkey;");
            stmt.execute("ALTER TABLE IF EXISTS public.productline DROP CONSTRAINT IF EXISTS productline_order_id_fkey;");
            stmt.execute("ALTER TABLE IF EXISTS public.productline DROP CONSTRAINT IF EXISTS productline_topping_id_fkey;");

            stmt.execute("DROP TABLE IF EXISTS public.bottom;");
            stmt.execute("CREATE TABLE IF NOT EXISTS public.bottom (" +
                    "bottom_id serial NOT NULL, " +
                    "bottom_name character varying(20) NOT NULL, " +
                    "bottom_price numeric NOT NULL, " +
                    "CONSTRAINT bottom_pkey PRIMARY KEY (bottom_id)" +
                    ");");

            stmt.execute("DROP TABLE IF EXISTS public.orders;");
            stmt.execute("CREATE TABLE IF NOT EXISTS public.orders (" +
                    "order_id integer NOT NULL DEFAULT nextval('order_order_id_seq'), " +
                    "order_price numeric NOT NULL, " +
                    "paid_status boolean NOT NULL DEFAULT false, " +
                    "user_id integer, " +
                    "CONSTRAINT order_pkey PRIMARY KEY (order_id)" +
                    ");");

            stmt.execute("DROP TABLE IF EXISTS public.productline;");
            stmt.execute("CREATE TABLE IF NOT EXISTS public.productline (" +
                    "productline_id integer NOT NULL DEFAULT nextval('product_line_productline_id_seq'::regclass), " +
                    "quantity integer NOT NULL DEFAULT 0, " +
                    "total_price numeric NOT NULL, " +
                    "topping_id integer NOT NULL, " +
                    "bottom_id integer NOT NULL, " +
                    "order_id integer NOT NULL, " +
                    "CONSTRAINT product_line_pkey PRIMARY KEY (productline_id)" +
                    ");");

            stmt.execute("DROP TABLE IF EXISTS public.topping;");
            stmt.execute("CREATE TABLE IF NOT EXISTS public.topping (" +
                    "topping_id serial NOT NULL, " +
                    "topping_name character varying(20) NOT NULL, " +
                    "topping_price numeric NOT NULL, " +
                    "CONSTRAINT topping_pkey PRIMARY KEY (topping_id)" +
                    ");");

            stmt.execute("DROP TABLE IF EXISTS public.users;");
            stmt.execute("CREATE TABLE IF NOT EXISTS public.users (" +
                    "user_id integer NOT NULL DEFAULT nextval('user_user_id_seq'::regclass), " +
                    "email character varying NOT NULL, " +
                    "user_password character varying NOT NULL, " +
                    "balance numeric NOT NULL DEFAULT 0, " +
                    "is_admin boolean NOT NULL DEFAULT false, " +
                    "CONSTRAINT user_pkey PRIMARY KEY (user_id)" +
                    ");");

            // Tilføj fremmednøgle-konstraints
            stmt.execute("ALTER TABLE IF EXISTS public.orders ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users (user_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID;");
            stmt.execute("ALTER TABLE IF EXISTS public.productline ADD CONSTRAINT productline_bottom_id_fkey FOREIGN KEY (bottom_id) REFERENCES public.bottom (bottom_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID;");
            stmt.execute("ALTER TABLE IF EXISTS public.productline ADD CONSTRAINT productline_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders (order_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID;");
            stmt.execute("ALTER TABLE IF EXISTS public.productline ADD CONSTRAINT productline_topping_id_fkey FOREIGN KEY (topping_id) REFERENCES public.topping (topping_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION NOT VALID;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


        @BeforeEach
    void setUp() {
    }

    @Test
    void testConnection() throws SQLException {
        assertNotNull(connectionPool.getConnection()); //Vi tester at når vi kalder vores connect() så returneres der ikke null
    }

    @Test
    void getOrderById() {
    }

    @Test
    void deleteOrder() {
    }

    @Test
    void createOrder() {
    }

    @Test
    void getOrdersByUser() {
    }

    @Test
    void getAllOrders() {
    }

    @Test
    void getProductLineByOrderId() {
    }

    @Test
    void setOrderStatus() {
    }
}