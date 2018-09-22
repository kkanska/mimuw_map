package kasia.mimuwmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplaySearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_result);

        Intent intent = getIntent();
        setProfessorName(intent);
        setRoomNumber(intent);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            //  TODO: fallback?
        }
    }

    private void setProfessorName(@NonNull Intent intent) {
        Professor professor = (Professor) intent.getSerializableExtra(FindProfessorActivity.PROFESSOR);
        setText(R.id.professorFullText, professor.getFullName());
    }

    private void setRoomNumber(@NonNull Intent intent) {
        Room room = (Room) intent.getSerializableExtra(FindProfessorActivity.ROOM);
        if (room == null) {
            Professor professor = (Professor) intent.getSerializableExtra(FindProfessorActivity.PROFESSOR);
            setText(R.id.professorRoomText, getString(R.string.display_room_unknown,
                    professor.getRoom()));
            return;
        }
        String roomDescription = getString(R.string.display_room_known,
                room.getNumber(), room.getFloor());
        setText(R.id.professorRoomText, roomDescription);

        int color = ContextCompat.getColor(this, R.color.colorAccent);
        MapDrawable mapWithPoint = new MapDrawable(this, color, room);
        ImageView image = findViewById(R.id.mapImageView);
        image.setImageDrawable(mapWithPoint);
    }

    private void setText(int id, String text) {
        TextView textView = findViewById(id);
        textView.setText(text);
    }
}
