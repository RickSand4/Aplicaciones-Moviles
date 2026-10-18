package com.upiiz.dm_eohs_07.data.db;

import androidx.room3.migration.Migration;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

public final class Migraciones {
    private Migraciones() {}

    public static final Migration VERSION_1_A_2 = new Migration(1, 2) {
        @Override public Object migrate(SQLiteConnection connection, Continuation<? super Unit> continuation) {
            ejecutar(connection, "ALTER TABLE mascotas ADD COLUMN usuario_id INTEGER NOT NULL DEFAULT 0");
            ejecutar(connection, "CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT, correo TEXT, password TEXT)");
            ejecutar(connection, "CREATE UNIQUE INDEX IF NOT EXISTS index_usuarios_correo ON usuarios (correo)");
            return Unit.INSTANCE;
        }
    };

    public static final Migration VERSION_2_A_3 = new Migration(2, 3) {
        @Override public Object migrate(SQLiteConnection connection, Continuation<? super Unit> continuation) {
            boolean existe;
            try (SQLiteStatement query = connection.prepare("SELECT name FROM sqlite_master WHERE type='table' AND name='veterinarios'")) {
                existe = query.step();
            }
            if (existe) ejecutar(connection, "ALTER TABLE veterinarios RENAME TO veterinarios_legacy");
            ejecutar(connection, "CREATE TABLE IF NOT EXISTS veterinarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, edad INTEGER NOT NULL, cedula TEXT)");
            if (existe) {
                ejecutar(connection, "INSERT INTO veterinarios (id, edad, cedula) SELECT id, edad, cedula FROM veterinarios_legacy");
                ejecutar(connection, "DROP TABLE veterinarios_legacy");
            }
            return Unit.INSTANCE;
        }
    };

    private static void ejecutar(SQLiteConnection connection, String sql) {
        try (SQLiteStatement statement = connection.prepare(sql)) {
            statement.step();
        }
    }
}
