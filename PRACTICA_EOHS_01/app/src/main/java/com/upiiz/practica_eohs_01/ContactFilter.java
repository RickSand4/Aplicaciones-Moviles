package com.upiiz.practica_eohs_01;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Filtering is independent of Android so its combination and limit can be tested. */
public final class ContactFilter {
    public static final int SEARCH_LIMIT = 5;
    private ContactFilter() { }

    private static String normalize(String value) {
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }

    public static List<Contact> apply(List<Contact> contacts, String query, String category) {
        List<Contact> result = new ArrayList<>();
        String criterion = normalize(query);
        for (Contact contact : contacts) {
            boolean matchesCategory = "Todos".equals(category)
                    || ("No leídos".equals(category) && contact.unread > 0)
                    || ("Favoritos".equals(category) && contact.favorite)
                    || ("Grupos".equals(category) && contact.group);
            if (matchesCategory && normalize(contact.name).contains(criterion)) {
                result.add(contact);
                if (!criterion.isEmpty() && result.size() == SEARCH_LIMIT) break;
            }
        }
        return result;
    }
}
