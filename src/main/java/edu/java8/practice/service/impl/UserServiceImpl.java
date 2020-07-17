package edu.java8.practice.service.impl;

import edu.java8.practice.domain.Privilege;
import edu.java8.practice.domain.User;
import edu.java8.practice.service.UserService;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.compare;

public class UserServiceImpl implements UserService {

    @Override
    public List<String> getFirstNamesReverseSorted(List<User> users) {

        return users.stream()
                .map(User::getFirstName)
                .sorted(Comparator.reverseOrder())
                .collect(toList());


    }

    @Override
    public List<User> sortByAgeDescAndNameAsc(final List<User> users) {

        return users.stream()
                .sorted(Comparator.comparing(User::getAge).reversed().thenComparing(User::getFirstName))
                .collect(toList());
    }

    @Override
    public List<Privilege> getAllDistinctPrivileges(final List<User> users) {

        return users.stream()
                .map(User::getPrivileges)
                .flatMap(x -> x.stream())
                .distinct()
                .collect(toList());
    }

    @Override
    public Optional<User> getUpdateUserWithAgeHigherThan(final List<User> users, final int age) {

        return users.stream()
                .filter(x -> x.getPrivileges().contains(Privilege.UPDATE) && x.getAge() > age)
                .findAny();
    }

    @Override
    public Map<Integer, List<User>> groupByCountOfPrivileges(final List<User> users) {

        return users.stream()
                .collect(Collectors.groupingBy(x -> x.getPrivileges().size()));

    }

    @Override
    public double getAverageAgeForUsers(final List<User> users) {

        return users.stream()
                .map(User::getAge)
                .mapToInt(x -> x)
                .average().orElse(-1);
    }

    @Override
    public Optional<String> getMostFrequentLastName(final List<User> users) {

        return users.stream()

                .collect(Collectors.groupingBy(User::getLastName, Collectors.counting()))
                .entrySet().stream().max(Comparator.comparing(Map.Entry::getKey))
                .filter(x-> !(x.getValue() ==1))
                .map(Map.Entry::getKey);
    }

    @Override
    public List<User> filterBy(final List<User> users, final Predicate<User>... predicates) {

        return users.stream()
                .filter(Arrays.stream(predicates).reduce(t -> true, Predicate::and))
                .collect(toList());

    }

    @Override
    public String convertTo(final List<User> users, final String delimiter, final Function<User, String> mapFun) {

        return users.stream()
                .map(x -> x.getLastName())
                .collect(Collectors.joining(delimiter));
    }

    @Override
    public Map<Privilege, List<User>> groupByPrivileges(List<User> users) {

        return users.stream()
        .flatMap(x -> x.getPrivileges().stream()
                .map(p -> new AbstractMap.SimpleEntry<>(p, x)))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

    }

    @Override
    public Map<String, Long> getNumberOfLastNames(final List<User> users) {

        return users.stream()
                .collect(Collectors.groupingBy(User::getLastName, Collectors.counting()));

    }
}
