package com.upiiz.examen_eohs_01;
import org.junit.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;

public class ExamRequirementsTest {
    @Test public void onlyExamCredentialsAreAccepted() {
        assertTrue(Validation.validLogin("admin", "123456"));
        assertFalse(Validation.validLogin("Admin", "123456"));
        assertFalse(Validation.validLogin("admin", "123456 "));
        assertFalse(Validation.validLogin("other", "123456"));
        assertFalse(Validation.validLogin("admin", "wrong"));
        assertFalse(Validation.validLogin("", ""));
        assertFalse(Validation.validLogin(null, null));
    }
    @Test public void nameMustHaveThreeRealCharacters() {
        assertFalse(Validation.validName("  "));
        assertFalse(Validation.validName(" Al "));
        assertTrue(Validation.validName("Ana"));
        assertTrue(Validation.validName(" José "));
        assertFalse(Validation.validName(null));
    }
    @Test public void passwordsMustMatchAndNotBeBlank() {
        assertTrue(Validation.matchingPasswords("secreto", "secreto"));
        assertFalse(Validation.matchingPasswords("secreto", "Secreto"));
        assertFalse(Validation.matchingPasswords("", ""));
        assertFalse(Validation.matchingPasswords("   ", "   "));
        assertFalse(Validation.matchingPasswords(null, null));
    }
    @Test public void twentyDistinctUsersCanBeSelected() {
        List<User> users = DemoData.users();
        assertEquals(20, users.size());
        Set<Integer> ids = new HashSet<>();
        Set<String> handles = new HashSet<>();
        for (User user : users) {
            assertTrue(ids.add(user.id));
            assertTrue(handles.add(user.username));
            assertEquals(user.name, DemoData.findUser(user.id).name);
        }
        assertNull(DemoData.findUser(-1));
    }
    @Test public void everyChatStartsWithTwentyAlternatingMessages() {
        for (User user : DemoData.users()) {
            List<Message> messages = DemoData.messages(user);
            assertEquals(20, messages.size());
            assertTrue(messages.get(0).text.contains(user.name.split(" ")[0]));
            for (int i = 0; i < messages.size(); i++) {
                assertEquals(i % 2 == 0, messages.get(i).outgoing);
                assertFalse(messages.get(i).text.isEmpty());
            }
            messages.remove(0);
            assertEquals(20, DemoData.messages(user).size());
        }
    }
}
