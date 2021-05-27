package com.kembibl.KemBibl;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kembibl.KemBibl.adapters.HelpListArrayAdapter;
import com.kembibl.KemBibl.models.Help_list_Model;
import com.kembibl.testbible3.R;

import java.util.ArrayList;
//Класс отвечает за вопросы и ответы
public class HelpActivity extends AppCompatActivity {
    ListView help_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Вопросы/Ответы");
        help_answer=(ListView)findViewById(R.id.helpList);
        String quest1, answer1;
        String quest2, answer2;
        String quest3, answer3;
        String quest4, answer4;
        String quest5, answer5;
        String quest6, answer6;
        String quest7, answer7;
        String quest8, answer8;
        String quest9, answer9;
        String quest10, answer10;
        String quest11, answer11;
        String quest12, answer12;
        String quest13, answer13;

        quest1="Вопрос 1 \"Как начать поиск книг?\"";
        answer1="Ответ 1: \"В главном меню выберите пункт \"Поиск книг\". В предложенном поле для поиска введите фамилию автора и/или в кавычках название книги. Можно ввести только фамилию автора или только в кавычках название книги. При наборе фамилии автора, выводится список возможных значений (автоподбор). Выберите нужного автора из появившегося списка или продолжайте ввод фамилии автора самостоятельно. Чтобы убрать выпадающий список, нажмите на строку ввода. Нажмите кнопку \"поиск\".\n" +
                "Примеры запросов:\n" +
                "1) Пушкин\n" +
                "2) Уэллс \"Машина времени\"\n" +
                "3) \"Российская газета\"";

        quest2="Вопрос 2 \"Какие виды поиска доступны в приложении?\"";
        answer2="Ответ 2: \"При нажатии на три вертикальных точки в правом верхнем углу окна приложения, появляется выбор из четырех видов поиска: \n" +
                " Точный поиск. Этот вид поиска предоставляет расширенные возможности. В нем можно отсортировать книги по году издания документа, теме, типу документа (статья, книга, видеозапись и др.).\"\n" +
                "Поиск по названию. В этом виде поиска название можно вводить без кавычек.\n" +
                "Поиск по автору. \n" +
                "Поиск одной строкой. Это тот тип поиска, что открывается при выборе пункта \"Поиск книг\" в главном меню.";

        quest3="Вопрос 3 \"Как посмотреть подробную информацию о книге в результатах поиска?\"";
        answer3="Ответ 3: \"Нажмите на краткое описание издания, появится подробная информация о книге и вкладка \"Экземпляр\" с информацией о том, в какой библиотеке находиться книга.";

        quest4="Вопрос 4 \"Как узнать где находится библиотека?\"";
        answer4="Ответ 4: \"В главном меню выберите \"Библиотеки\". Откроется список названий библиотек с указанием адреса. Чтобы найти библиотеку на карте города, нажмите на ее название, далее будет предложено выбрать приложение для просмотра карты. Рекомендуется открывать через 2Гис.\"";

        quest5="Вопрос 5 \"Как забронировать книгу?\"";
        answer5="Ответ 5: Услуга доступна только читателям библиотек МАУК \"МИБС\". Для того, чтобы забронировать книгу, необходимо авторизоваться в \"Личном кабинете\". Для этого введите логин (номер (штрих-код) читательского билета) и пароль (заданный при записи в библиотеку). Проведите поиск книг. Забронировать издание можно из результата простого или точного поиска. Кликнете на нужное издание, выберите вкладку \"Экземпляр\". При наличии экземпляра вам будет предложен список библиотек, выберите нужную. Ответьте утвердительно на предложения заказать книгу. Книга будет забронирована.\"";

        quest6="Вопрос 6 \"Как посмотреть текущие забронированные документы?\"";
        answer6="Ответ 6: \"Войдите в Личный кабинет. Перейдите на вкладку \"Текущие заказы\". Нажмите \"Показать забронированные документы\". Перед вами список забронированных документов с указание даты и времени окончания брони.\"";

        quest7="Вопрос 7 \"Как посмотреть текущие выдачи?\"";
        answer7="Ответ 7: \"Войдите в Личный кабинет. Перейдите на вкладку \"Текущие заказы\". Нажмите \"Показать текущие выдачи\". Перед вами список документов, выданных вам на данный момент.\"";

        quest8="Вопрос 8 \"Как посмотреть ранее взятые книги?\"";
        answer8="Ответ 8: \"Войдите в Личный кабинет. Перейдите на вкладку \"История\". Нажмите \"Показать ранее взятые книги\". Здесь показаны документы, которые были когда-либо взяты вами в библиотеке.\"";

        quest9="Вопрос 9 \"Как посмотреть историю бронирований?\"";
        answer9="Ответ 9: \"Войдите в Личный кабинет. Перейдите на вкладку \"История\". Нажмите \"Показать историю бронирований\". Здесь будут показаны документы, которые были когда-либо забронированы вами.\"";

        quest10="Вопрос 10 \"Как добавить книгу в избранное?\" ";
        answer10="Ответ 10: \"Для добавления книги в избранное, необходимо сначала авторизоваться в личном кабинете. Найдите книгу, на вкладке «Библ. описание» доступна кнопка \"На полку\". При нажатии на эту кнопку, и книга добавится в список избранных. Посмотреть избранные книги можно в личном кабинете во вкладке Полка.\"";

        quest11="Вопрос 11 \"Как посмотреть избранные книги?\"";
        answer11="Ответ 11: \"Войдите в Личный кабинет. Перейдите на вкладку \"Полка\". Здесь будут показаны избранные документы, запланированные вами для прочтения.\"";

        quest12="Вопрос 12 \"Как удалить избранные книги?\"";
        answer12="Ответ 12: \"Войдите в Личный кабинет. Перейдите на вкладку \"Полка\". Выберите нужную книгу и нажмите на кнопку \"Удалить с полки.\"";

        quest13="Вопрос 13 \"Как посмотреть подробную информацию о книге в Личном кабинете?\"";
        answer13="Ответ 13: \"Войдите в Личный кабинет. Перейдите на нужную вкладку. Нажмите на краткое описание издание, появится подробная информация о книге. Вы сможете вновь забронировать книгу или добавить в избранное на Полку. В разделе \"Полка\", у нужного экземпляра необходимо нажать кнопку \"Открыть\".\"";

        ArrayList<Help_list_Model> HelpList=new ArrayList<>();
        String Image="help_icon";
        HelpList.add(new Help_list_Model(quest1,answer1 , Image));
        HelpList.add(new Help_list_Model(quest2,answer2 , Image));
        HelpList.add(new Help_list_Model(quest3,answer3 , Image));
        HelpList.add(new Help_list_Model(quest4,answer4 , Image));
        HelpList.add(new Help_list_Model(quest5,answer5 , Image));
        HelpList.add(new Help_list_Model(quest6,answer6 , Image));
        HelpList.add(new Help_list_Model(quest7,answer7 , Image));
        HelpList.add(new Help_list_Model(quest8,answer8 , Image));
        HelpList.add(new Help_list_Model(quest9,answer9 , Image));
        HelpList.add(new Help_list_Model(quest10,answer10 , Image));
        HelpList.add(new Help_list_Model(quest11,answer11 , Image));
        HelpList.add(new Help_list_Model(quest12,answer12 , Image));
        HelpList.add(new Help_list_Model(quest13,answer13 , Image));
        ArrayAdapter<Help_list_Model> myAdapter = new HelpListArrayAdapter(this, 0, HelpList);
        help_answer.setAdapter(myAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //this.finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
