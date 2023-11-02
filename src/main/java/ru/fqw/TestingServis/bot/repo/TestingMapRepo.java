package ru.fqw.TestingServis.bot.repo;

import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.bot.models.TestFromTelegramUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestingMapRepo implements TestingRepo{
private Map<Long, TestFromTelegramUser> testMap = new HashMap<>();
    @Override
    public void save(Long chatId, TestFromTelegramUser testFromTelegramUser) {
        testMap.put(chatId,testFromTelegramUser);
    }
    @Override
    public void delite(Long chatId) {
        testMap.remove(chatId);
    }
    @Override
    public TestFromTelegramUser get(Long chatId) {
        return testMap.get(chatId);
    }
    @Override
    public boolean isUserHaveTest(Long chatId) {
        return testMap.get(chatId) != null;
    }
    @Override
    public List<TestFromTelegramUser> getAll(){
        return new ArrayList<>(testMap.values());
    }
    @Override
    public  List<Long> getChatIdsByTest(TestFromTelegramUser test){
       return testMap
                .entrySet()
                .stream()
                .filter(entry -> test.equals(entry.getValue()))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
