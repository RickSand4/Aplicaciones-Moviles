package com.upiiz.practica_eohs_01;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class ContactFilterTest {
    private Contact contact(String name, int unread, boolean group, boolean favorite) {
        return new Contact(name,name,"","","",0,unread,group,favorite);
    }
    @Test public void searchIsLimitedButEmptySearchShowsAll() {
        List<Contact> people = new ArrayList<>();
        for (int i=0;i<12;i++) people.add(contact("Ana " + i,0,false,false));
        assertEquals(5,ContactFilter.apply(people,"ana","Todos").size());
        assertEquals(12,ContactFilter.apply(people,"   ","Todos").size());
    }
    @Test public void matchesAccentsCaseAndWhitespace() {
        List<Contact> people = Arrays.asList(contact("Sofía López",1,false,true));
        assertEquals(1,ContactFilter.apply(people," SOFIA ","Todos").size());
        assertEquals(0,ContactFilter.apply(people,"Diego","Todos").size());
    }
    @Test public void combinesSearchWithEachCategory() {
        List<Contact> people = Arrays.asList(contact("Ana",2,false,true),contact("Ana grupo",0,true,false));
        assertEquals("Ana",ContactFilter.apply(people,"ana","No leídos").get(0).name);
        assertEquals(1,ContactFilter.apply(people,"ana","Favoritos").size());
        assertEquals("Ana grupo",ContactFilter.apply(people,"ana","Grupos").get(0).name);
        assertTrue(ContactFilter.apply(people,"nadie","Grupos").isEmpty());
    }
    @Test public void limitAppliesAfterCategoryFiltering() {
        List<Contact> people = new ArrayList<>();
        for (int i=0;i<10;i++) people.add(contact("Ana " + i,0,false,false));
        people.add(contact("Ana favorita",1,false,true));
        assertEquals(1,ContactFilter.apply(people,"ana","Favoritos").size());
        assertEquals(11,people.size());
    }
}
