package com.upiiz.examen_eohs_01;

import android.content.Intent;
import android.widget.ListView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExamFlowTest {
    @Test public void loginRejectsIncorrectCredentialsAndOpensSelectedChat() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.username)).perform(scrollTo(), replaceText("admin"));
            onView(withId(R.id.password)).perform(scrollTo(), replaceText("wrong"), closeSoftKeyboard());
            onView(withId(R.id.login_button)).perform(scrollTo(), click());
            onView(withText(R.string.invalid_login)).check(matches(isDisplayed()));
            onView(withId(R.id.password)).perform(scrollTo(), replaceText("123456"), closeSoftKeyboard());
            onView(withId(R.id.login_button)).perform(scrollTo(), click());
            onView(withId(R.id.users_list)).check((view, error) -> {
                if (error != null) throw error;
                assertEquals(20, ((ListView) view).getAdapter().getCount());
            });
            onData(anything()).inAdapterView(withId(R.id.users_list)).atPosition(19).perform(click());
            onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Ricardo Reyes"))));
            onView(withId(R.id.messages_list)).check((view, error) -> {
                if (error != null) throw error;
                MessageAdapter adapter = (MessageAdapter) ((ListView) view).getAdapter();
                assertEquals(20, adapter.getCount());
                for (int i = 0; i < 20; i++) assertEquals(i % 2 == 0 ? 1 : 0, adapter.getItemViewType(i));
            });
            onData(anything()).inAdapterView(withId(R.id.messages_list)).atPosition(19)
                    .check(matches(hasDescendant(withText("De acuerdo, ¡nos vemos al rato!"))));
            pressBack();
            onView(withId(R.id.users_list)).check(matches(isDisplayed()));
            pressBack();
            onView(withId(R.id.login_button)).check(matches(isDisplayed()));
        }
    }

    @Test public void registrationValidatesAndBothLinksReturnToLogin() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.create_account)).perform(scrollTo(), click());
            onView(withId(R.id.full_name)).perform(scrollTo(), replaceText("Al"));
            onView(withId(R.id.password)).perform(scrollTo(), replaceText("secret"));
            onView(withId(R.id.confirm_password)).perform(scrollTo(), replaceText("different"), closeSoftKeyboard());
            onView(withId(R.id.register_button)).perform(scrollTo(), click());
            onView(withText(R.string.invalid_name)).perform(scrollTo()).check(matches(isDisplayed()));
            onView(withText(R.string.password_mismatch)).perform(scrollTo()).check(matches(isDisplayed()));
            onView(withId(R.id.full_name)).perform(scrollTo(), replaceText("Ana"));
            onView(withId(R.id.confirm_password)).perform(scrollTo(), replaceText("secret"), closeSoftKeyboard());
            onView(withId(R.id.register_button)).perform(scrollTo(), click());
            onView(withId(R.id.login_button)).check(matches(isDisplayed()));
            onView(withId(R.id.create_account)).perform(scrollTo(), click());
            onView(withId(R.id.have_account)).perform(scrollTo(), click());
            onView(withId(R.id.login_button)).check(matches(isDisplayed()));
        }
    }

    @Test public void sendingIgnoresBlankAndSurvivesRecreation() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class);
        intent.putExtra(ChatActivity.USER_ID, 1);
        try (ActivityScenario<ChatActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.message_input)).perform(replaceText("   "), closeSoftKeyboard());
            onView(withId(R.id.send_button)).perform(click());
            onView(withId(R.id.messages_list)).check((view, error) -> {
                if (error != null) throw error;
                assertEquals(20, ((ListView) view).getAdapter().getCount());
            });
            onView(withId(R.id.message_input)).perform(replaceText("Mensaje de prueba"), closeSoftKeyboard());
            onView(withId(R.id.send_button)).perform(click());
            scenario.recreate();
            onView(withId(R.id.messages_list)).check((view, error) -> {
                if (error != null) throw error;
                MessageAdapter adapter = (MessageAdapter) ((ListView) view).getAdapter();
                assertEquals(21, adapter.getCount());
                assertEquals("Mensaje de prueba", adapter.getItem(20).text);
                assertTrue(adapter.getItem(20).outgoing);
            });
        }
    }
}
