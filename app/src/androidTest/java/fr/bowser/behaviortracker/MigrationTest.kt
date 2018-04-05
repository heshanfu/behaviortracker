package fr.bowser.behaviortracker

import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.testing.MigrationTestHelper
import android.graphics.Color
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import fr.bowser.behaviortracker.database.DatabaseManager.Companion.MIGRATION_1_2
import fr.bowser.behaviortracker.database.DatabaseProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @Rule
    lateinit var helper: MigrationTestHelper

    @Before
    fun init() {
        helper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                DatabaseProvider::class.java.canonicalName,
                FrameworkSQLiteOpenHelperFactory())
    }

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        val db = helper.createDatabase(TEST_DB, 1)

        val timerId = 0
        val timerCurrentTime = 0
        val timerName = "MyTimer"
        val timerColor = Color.RED
        db.execSQL("INSERT INTO Timer VALUES (:timerId, :timerCurrentTime, :timerName, :timerColor)")

        db.close()

        helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        helper.
    }

    companion object {
        private const val TEST_DB = "migration-test"
    }

}
