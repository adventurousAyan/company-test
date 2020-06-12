package com.company;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DatingRecommendationService {

    public List<User> getMatches(User px, List<User> users, int limit) throws DatingRecommendationValidationException {
        if (users == null)
            throw new DatingRecommendationValidationException(TDDConstants.NO_MATCHES);
        if (users.size() == 0)
            throw new DatingRecommendationValidationException(TDDConstants.NO_MATCHES);
        if (px == null)
            throw new DatingRecommendationValidationException(TDDConstants.INPUT_USER_EMPTY);
        if (users != null && limit > users.size())
            throw new DatingRecommendationValidationException(TDDConstants.LIMIT_INVALID);
        List<User> usersBasedOnGender = getUsersBasedOnGender(px, users);
        List<User> usersBasedOnAge = getUsersBasedOnAge(px, usersBasedOnGender);
        List<User> usersBasedOnInterest = getUsersBasedOnInterest(px, usersBasedOnAge);
        usersBasedOnInterest = usersBasedOnInterest.stream().limit(limit).collect(Collectors.toList());
        return usersBasedOnInterest;
    }

    private List<User> getUsersBasedOnGender(User px, List<User> users) {
        String gender = px.getGender();
        List<User> outUsers = users.stream()
                .filter(p -> !p.equals(px))
                .filter(x -> !x.getGender().equals(gender))
                .collect(Collectors.toList());
        if (outUsers.size() == 0)
            outUsers = users.stream()
                    .filter(p -> !p.equals(px))
                    .collect(Collectors.toList());
        return outUsers;
    }

    private List<User> getUsersBasedOnAge(User px, List<User> users) {
        int age = px.getAge();
        return users.stream()
                .filter(p -> !p.equals(px))
                .sorted(Comparator.comparingInt(p -> Math.abs(p.getAge() - age)))
                .collect(Collectors.toList());
    }

    private List<User> getUsersBasedOnInterest(User px, List<User> users) {
        List<String> interests = px.getInterests();
        return users.stream()
                .filter(p -> !p.equals(px))
                .sorted(Comparator.comparing(p -> interests.indexOf(p)).reversed())
                .collect(Collectors.toList());
    }
}
