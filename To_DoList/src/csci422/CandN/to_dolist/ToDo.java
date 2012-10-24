/*
 * Chris Card
 * Nathan Harvey
 * 10/24/12
 */
package csci422.CandN.to_dolist;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ToDo extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_to_do, menu);
        return true;
    }
}
