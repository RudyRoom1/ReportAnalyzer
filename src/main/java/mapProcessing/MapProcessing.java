package mapProcessing;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.join;

public class MapProcessing {

    public static String getSubstring(String regex, String pathName) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pathName);
        if (matcher.find()) {
            pathName = matcher.group();
        }
        return pathName;
    }

    private static List<String> cutFileValue(String regex, String pathName) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pathName);
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            String matches = matcher.group();
            result.add(matches.replaceAll("\\s", "").substring(matches.indexOf(":") + 1));
        }
        return result;
    }

    public static Map<String, List<String>> pathKeys(String keyRegex, String valueRegex, Map<String, String> keysOfPath) {
        return keysOfPath.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> String.format("%s:", getSubstring(keyRegex, entry.getKey())),
                        entry -> cutFileValue(valueRegex, entry.getValue())
                ));
    }

    public static String concatKeyAndValueWithSeparate(Map<String, List<String>> keysOfPath, String separator) {
        Set<Map.Entry<String, List<String>>> set = keysOfPath.entrySet();
        List<String> list = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : set) {
            list.add(entry.getKey().concat(String.join(separator, entry.getValue())));
        }
        return String.join("\n", list);
    }

    public static List<String> nameOfSuite(List<String> list, String regex) {
        return list.stream().filter(e -> e.contains(regex)).collect(Collectors.toList());
    }

    public static Map<String, List<String>> filteringMapOfList(Map<String, List<String>> keysOfPath, List<String> skus) {
        Set<Map.Entry<String, List<String>>> set = keysOfPath.entrySet();
        Map<String, List<String>> res = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : set) {
            List<String> commonElements = skus.stream()
                    .filter(entry.getValue()::contains)
                    .collect(Collectors.toList());
            if (!commonElements.isEmpty())
                res.put(entry.getKey(), commonElements);
        }
        return res;
    }

    public static List<String> containSku(List<String> firstList, List<String> secondList) {
        List<String> result;
        result = firstList.stream().filter(secondList::contains).collect(Collectors.toList());
        return result;
    }

    public static String filteringOfSku(List<String> nameOfSku, List<String> allSkuData) {
        List<String> result = new LinkedList<>();
        for (String entity : allSkuData) {
            for (String sku : nameOfSku) {
                String skuPartOfData = entity.substring(entity.indexOf(":"));
                if (skuPartOfData.contains(":" + sku))
                    result.add(entity);
            }
        }
        return join("\n", result).replace("\\", "/");
    }

    public static List<String> concatKeyAndValue(Map<String, List<String>> keysOfPath) {
        Set<Map.Entry<String, List<String>>> set = keysOfPath.entrySet();
        List<String> list = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : set) {
            for (String entity : entry.getValue()) {
                list.add(entry.getKey().concat(entity));
            }
        }
        return list;
    }
}


