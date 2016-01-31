package com.ga1.hsktextdifficultychecker;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class HskTextDifficultyCheckerMainActivity extends Activity {

    private HskDatabase hskDb;

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        hskDb = new HskDatabase(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

    }

    public void clear(View v) {
        EditText editText = ((EditText) (findViewById(R.id.editText)));
        editText.setText("");
        TextView hsk1textView = ((TextView) (findViewById(R.id.hsk1)));
        TextView hsk2textView = ((TextView) (findViewById(R.id.hsk2)));
        TextView hsk3textView = ((TextView) (findViewById(R.id.hsk3)));
        TextView hsk4textView = ((TextView) (findViewById(R.id.hsk4)));
        TextView hsk5textView = ((TextView) (findViewById(R.id.hsk5)));
        TextView hsk6textView = ((TextView) (findViewById(R.id.hsk6)));
        hsk6textView.setText("HSK6 = " + 0);
        hsk5textView.setText("HSK5 = " + 0);
        hsk4textView.setText("HSK4 = " + 0);
        hsk3textView.setText("HSK3 = " + 0);
        hsk2textView.setText("HSK2 = " + 0);
        hsk1textView.setText("HSK1 = " + 0);
    }

    public void countAndColorHskCharacters(View v) {
        colorCharacters();
        countCharacters();
    }

    private void countCharacters() {
        TextView hsk1textView = ((TextView) (findViewById(R.id.hsk1)));
        TextView hsk2textView = ((TextView) (findViewById(R.id.hsk2)));
        TextView hsk3textView = ((TextView) (findViewById(R.id.hsk3)));
        TextView hsk4textView = ((TextView) (findViewById(R.id.hsk4)));
        TextView hsk5textView = ((TextView) (findViewById(R.id.hsk5)));
        TextView hsk6textView = ((TextView) (findViewById(R.id.hsk6)));
        EditText editText = ((EditText) (findViewById(R.id.editText)));
        Editable text = editText.getText();
        int[] counters = new int[7];
        for (int i = 0; i < text.length(); i++)
            counters[hskDb.getLevel(text.charAt(i))]++;
        int sumOfCounters = 0;
        for (int c: counters)
            sumOfCounters += c;
        hsk6textView.setText("HSK6 = " + counters[6]);
        hsk5textView.setText("HSK5 = " + counters[5]);
        hsk4textView.setText("HSK4 = " + counters[4]);
        hsk3textView.setText("HSK3 = " + counters[3]);
        hsk2textView.setText("HSK2 = " + counters[2]);
        hsk1textView.setText("HSK1 = " + counters[1]);

    }

    private void colorCharacters() {
        EditText editText = ((EditText) (findViewById(R.id.editText)));
        Editable text = editText.getText();
        StringBuilder coloredChars = new StringBuilder("");
        for (int i = 0; i < text.length(); i++) {
            int color;
            char c = text.charAt(i);
            switch (hskDb.getLevel(c)) {
                case 1:  color = ColorConstants.HSK1;
                    break;
                case 2:  color = ColorConstants.HSK2;
                    break;
                case 3:  color = ColorConstants.HSK3;
                    break;
                case 4:  color = ColorConstants.HSK4;
                    break;
                case 5:  color = ColorConstants.HSK5;
                    break;
                case 6:  color = ColorConstants.HSK6;
                    break;
                default: color = ColorConstants.nonHSK;
                    break;
            }
            coloredChars.append("<font color=\"" + color + "\">" + c + "</font>");
        }
        int selectStart = editText.getSelectionStart();
        int selectEnd = editText.getSelectionEnd();
        editText.setText(Html.fromHtml(coloredChars.toString()));
//        editText.setSelection(selectStart, selectEnd);
    }

}