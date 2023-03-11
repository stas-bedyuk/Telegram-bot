package org.tg;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnglishQuizBot extends TelegramLongPollingBot {

     // Отправка сообщения пользователю

    public void sendMessage(String messageText, long chatId) {
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
                message.setChatId( Long.toString(chatId));
                message.setText(messageText);
        // создаем кнопку
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("QUIZ");
        button.setCallbackData("quiz");

// создаем ряд кнопок и добавляем туда нашу кнопку
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);

// создаем клавиатуру и добавляем туда ряд кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

// создаем объект InlineKeyboardMarkup и добавляем туда клавиатуру
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
public void createLevelsButtons(long chatId){
    // создаем кнопку

    InlineKeyboardButton button1 = new InlineKeyboardButton();
    button1.setText("A1");
    button1.setCallbackData("a1");
    InlineKeyboardButton button2 = new InlineKeyboardButton();
    button2.setText("A2");
    button2.setCallbackData("a2");
    InlineKeyboardButton button3 = new InlineKeyboardButton();
    button3.setText("B1");
    button3.setCallbackData("b1");
    InlineKeyboardButton button4 = new InlineKeyboardButton();
    button4.setText("B2");
    button4.setCallbackData("b2");
    InlineKeyboardButton button5 = new InlineKeyboardButton();
    button5.setText("C1");
    button5.setCallbackData("c1");
// создаем ряд кнопок и добавляем туда нашу кнопку
    List<InlineKeyboardButton> row0 = new ArrayList<>( );
    List<InlineKeyboardButton> row1 = new ArrayList<>();
    row0.add(button1);
    row0.add(button2);
    row1.add(button3);
    row1.add(button4);
    row1.add(button5);
// создаем клавиатуру и добавляем туда ряд кнопок
    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    keyboard.add(row0);
    keyboard.add(row1);
// создаем объект InlineKeyboardMarkup и добавляем туда клавиатуру
    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
    markup.setKeyboard(keyboard);
    SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
    message.setChatId(Long.toString(chatId));
    message.setReplyMarkup(markup);
    message.setText("Выберите приблизительный уровень ваших знаний, где А1 - минимальный С1-максимальный.");
    try {
        execute(message);
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }

}
    public void sendQuiz(long chatId, String question, List<String> options, int correctOptionIndex) {
        SendPoll quiz = new SendPoll();
        quiz.setChatId(Long.toString(chatId));
        quiz.setQuestion(question);
        quiz.setOptions(options);
        quiz.setType("quiz");
        quiz.setIsAnonymous(false);
        quiz.setCorrectOptionId(correctOptionIndex);
        // создаем кнопку
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("STOP");
        button.setCallbackData("stop");

// создаем ряд кнопок и добавляем туда нашу кнопку
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);

// создаем клавиатуру и добавляем туда ряд кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

// создаем объект InlineKeyboardMarkup и добавляем туда клавиатуру
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);


        quiz.setReplyMarkup(markup);

        try {
            execute(quiz);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {

        if (update.hasPollAnswer()) {
            long chatId = update.getPollAnswer().getUser().getId();
            List<Integer> optionIds = update.getPollAnswer().getOptionIds();
            int[] counter =  Counter.getInstance().get(chatId);
            if (optionIds.contains(Context.getInstance().get( chatId))) {
                counter[0] += 1;
                counter[1] += 1;
                Counter.getInstance().put(chatId, counter);

            } else {
                counter[1] += 1;
                Counter.getInstance().put(chatId, counter);
            }
            if(Counter.getInstance().get(chatId) != null) logic(counter[2],chatId);

        }
       else if (update.hasCallbackQuery()) {
            // Обработка нажатия кнопки
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData(); // получаем данные, связанные с кнопкой
            long chatId = callbackQuery.getMessage().getChatId(); // получаем ID чата
            int messageId = callbackQuery.getMessage().getMessageId(); // получаем ID сообщения
            switch (data){
                case "quiz":
                    createLevelsButtons(chatId);
                    break;
                case "stop":
                    int[] counter = Counter.getInstance().get(chatId);
                    sendMessage("Поздравляю тебя с окончанием теста! \nСегодня ты стал на шаг ближе к своей цели. \nТвой результат: "
                            + counter[0] + " из " + counter[1], chatId);
                    Counter.getInstance().delete(chatId);
                    break;
                case "a1":
                    createNewUser(1,chatId);
                    logic(1,chatId);
                    break;
                case "a2":
                    createNewUser(2,chatId);
                    logic(2,chatId);
                    break;
                case "b1":
                    createNewUser(3,chatId);
                    logic(3,chatId);
                    break;
                case "b2":
                    createNewUser(4,chatId);
                    logic(4,chatId);
                    break;
                case "c1":
                    createNewUser(5,chatId);
                    logic(5,chatId);
                    break;
            }


        }
       else if (update.hasMessage() && update.getMessage().hasText() ) {
            Message message = update.getMessage();
            long chatId = message.getChatId();




            // Обработка команды /start
            if (message.getText().equals("/start")) {
                //Получаем объект User, представляющий пользователя, отправившего сообщение
                User user = message.getFrom();

                sendMessage("Привет, " + user.getFirstName() +"!" + " Давай проверим твой уровень английского. " +
                        "Нажми  'QUIZ', чтобы начать. Когда захочешь закончить нажми 'STOP' , чтобы узнать результат.", chatId);
            }
        }
    }
public int[] createNewUser(int level,long chatId){
    if (Counter.getInstance().get(chatId) == null) {
        int[] count = new int[3];
        count[0] = 0;
        count[1] = 0;
        count[2] = level;
        Counter.getInstance().put(chatId, count);
        return count;
    }
    return null;
}


    // Получение имени бота, указанного в BotFather
public void logic( int level,long chatId){
    long startTime = System.nanoTime();

            // Получение случайного вопроса из базы данных
            String question = "Выберите перевод слова '";
            String[] variants_en = new String[10];
            String[] variants_ru = new String[10];
            int i = 0;

            RandomNumber a = new RandomNumber();
            RandomNumber random = null;

    int IDs  = random.getRandomNumberInt(1, Constants.counts[level-1]);
    if(IDs >Constants.counts[level-1]-10) {
        while(IDs >Constants.counts[level-1]-10) IDs--;
    }

            try {
                ConnectionToDataBase connection = new ConnectionToDataBase();
                Connection conn = connection.Connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + Constants.namesDB[level-1]);
                int id =0;
                while (rs.next()) {
                    id++;
                    if (IDs ==id  || i >0) {
                        variants_ru[i] = rs.getString(1);
                        variants_en[i] = rs.getString(2);
                        i++;
                    }
                    if(i == 10) break;

                }

                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            i = random.getRandomNumberInt(0, 9);

            // Формирование сообщения с вопросом и вариантами ответа
    // создание вариантов ответов
    List<String> options = new ArrayList<>();
    options.add("1.   " + variants_ru[0]);
    options.add("2.   " + variants_ru[1]);
    options.add("3.   " + variants_ru[2]);
    options.add("4.   " + variants_ru[3]);
    options.add("5.   " + variants_ru[4]);
    options.add("6.   " + variants_ru[5]);
    options.add("7.   " + variants_ru[6]);
    options.add("8.   " + variants_ru[7]);
    options.add("9.   " + variants_ru[8]);
    options.add("10.  " + variants_ru[9]);
    question += variants_en[i] + "' :";

           sendQuiz(chatId,question,options,i);

            // Сохранение правильного ответа в контексте пользователя


            Context.getInstance().put(chatId, i);

    long endTime = System.nanoTime();
    System.out.println(endTime -startTime);
        }


    public String getBotUsername() {
        return "EnglishQuizBot";
    }

    // Получение токена бота, указанного в BotFather
    @Override
    public String getBotToken() {
        return "6059266827:AAEBgd8fjmuS-6xS_X57Cq7X_1VC_AAQw8g";
    }
}
