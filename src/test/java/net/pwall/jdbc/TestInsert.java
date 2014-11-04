/*
 * @(#) TestInsert.java
 */

package net.pwall.jdbc;

import static org.junit.Assert.*;

import org.junit.Test;



/**
 * Default comment for {@code TestInsert}.
 *
 * @author  Peter Wall
 */
public class TestInsert {

    @Test
    public void test() {
        InsertStatement is = new InsertStatement(null, "ledger");
        is.addIntegerColumn("id", 1);
        assertEquals("insert into ledger (id) values (1)", is.toSQL());
        is.addStringColumn("user", "pwall");
        assertEquals("insert into ledger (id, user) values (1, 'pwall')", is.toSQL());
        is.addParameterColumn("balance");
        assertEquals("insert into ledger (id, user, balance) values (1, 'pwall', ?)",
                is.toSQL());
    }

    @Test
    public void test2() {
        InsertStatement is = new InsertStatement(null, "ledger");
        is.addIntegerColumn("id", 1);
        is.addStringColumn("user", "pwall");
        is.addParameterColumn("balance");

    }

}
