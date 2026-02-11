package com.gnews;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Exemplo INTENCIONALMENTE vulnerável (para laboratório):
 * SQL Injection por concatenação de string.
 */
public class VulnerableUserSearch {

    public static String findUserByName(String name) throws Exception {
        // Banco em memória só para exemplo (não precisa rodar de verdade pro review pegar)
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");

        // ❌ VULNERÁVEL: concatena entrada do usuário direto no SQL
        String sql = "SELECT * FROM users WHERE name = '" + name + "'";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        return rs.next() ? "FOUND" : "NOT_FOUND";
    }
}
