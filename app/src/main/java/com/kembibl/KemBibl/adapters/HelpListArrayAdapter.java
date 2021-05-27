package com.kembibl.KemBibl.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kembibl.KemBibl.models.Help_list_Model;
import com.kembibl.testbible3.R;

import java.util.ArrayList;
import java.util.List;

public class HelpListArrayAdapter extends ArrayAdapter<Help_list_Model> {
    private Context context;
    private List<Help_list_Model> Help_list_Model;

    // Конструктор, вызывается при создании
    public HelpListArrayAdapter(Context context, int resource, ArrayList<Help_list_Model> objects) {
        super(context, resource, objects);

        this.context = context;
        this.Help_list_Model = objects;
    }


    // вызывается, когда генерируется список
    public View getView(int position, View convertView, ViewGroup parent) {


        // получаем свойство, которое потом отображаем
        final Help_list_Model help = Help_list_Model.get(position);

        // получаем контекст со страницы списка вопросов
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.helplist_layout, null);

        TextView question = (TextView) view.findViewById(R.id.question);
        TextView answer = (TextView) view.findViewById(R.id.answer);

        ImageView image = (ImageView) view.findViewById(R.id.image);


        // установка атрибутов
        question.setText(help.getQuestion());
        answer.setText(help.getAnswer());


        //получаем и устанавливаем изображение
        int imageID = context.getResources().getIdentifier(help.getImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}
