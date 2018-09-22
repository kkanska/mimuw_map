package kasia.mimuwmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class FindProfessorActivity extends AppCompatActivity {
    public static final String PROFESSOR = "kasia.mimuwmap.PROFESSOR";
    public static final String ROOM = "kasia.mimuwmap.ROOM";
    private static final String PROFESSORS_FILE = "professors.csv";
    protected List<Professor> professors;
    private Map<Integer, Room> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_professor);

        try {
            parseDataFromResources();
        } catch (IOException e) {
            showExitInfo(getString(R.string.data_loading_error_message));
        }
        setDropdownList();
    }

    private void parseDataFromResources() throws IOException {
        rooms = RoomsReader.getRooms(getResources().openRawResource(R.raw.rooms));
        InputStream professorsFile = getProfessorsOutputFile();
        professors = ProfessorsReader.getProfessors(professorsFile);
    }

    private InputStream getProfessorsOutputFile() throws IOException {
        InputStream professorsFile;
        try {
            professorsFile = openFileInput(PROFESSORS_FILE);
        } catch (FileNotFoundException e) {
            try (InputStream inStream = getResources().openRawResource(R.raw.professors)) {
                try (FileOutputStream outStream = openFileOutput(PROFESSORS_FILE,
                        Context.MODE_PRIVATE)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inStream.read(buf)) > 0) {
                        outStream.write(buf, 0, len);
                    }
                    inStream.close();
                    outStream.close();
                }
            }
            professorsFile = openFileInput(PROFESSORS_FILE);
        }
        return professorsFile;
    }

    private void setDropdownList() {
        ArrayAdapter<Professor> adapter = new ProfessorArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, professors);
        AutoCompleteTextView textView = findViewById(R.id.professorInputTextView);
        textView.setAdapter(adapter);
    }

    /**
     * Called when the user taps the Find button
     */
    public void searchProfessor(View view) {
        AutoCompleteTextView editText = findViewById(R.id.professorInputTextView);
        String typedName = editText.getText().toString();
        Professor professor = getProfessor(typedName);
        if (professor == null) {
            showAddProfessorDialog(typedName);
        } else {
            showSearchResults(professor);
        }
    }

    @Nullable
    private Professor getProfessor(String fullName) {
        for (Professor professor : professors) {
            if (professor.toString().equalsIgnoreCase(fullName))
                return professor;
        }
        return null;
    }

    private void showAddProfessorDialog(final String fullName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_prof_dialog_title)
                .setMessage(getString(R.string.add_prof_dialog_message, fullName));
        builder.setPositiveButton(R.string.add_prof_dialog_ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FindProfessorService.startActionFindProfessor(
                        FindProfessorActivity.this,
                        fullName,
                        new ProfessorResultReceiver(FindProfessorActivity.this));
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSearchResults(@NonNull Professor professor) {
        Intent intent = new Intent(this, DisplaySearchResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROFESSOR, professor);
        bundle.putSerializable(ROOM, rooms.get(professor.getRoom()));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showActualizationInfo(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showExitInfo(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FindProfessorActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void addProfessor(Professor professor) {
        professors.add(professor);
    }

    private static class ProfessorResultReceiver implements FindProfessorResultReceiver.ReceiverCallback {

        private WeakReference<FindProfessorActivity> activityRef;

        public ProfessorResultReceiver(FindProfessorActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(Professor professor) {
            if (activityRef != null && activityRef.get() != null) {
                activityRef.get().addProfessor(professor);
                activityRef.get().setDropdownList();
                try {
                    FileOutputStream outputStream = activityRef.get().openFileOutput(PROFESSORS_FILE,
                            Context.MODE_PRIVATE);
                    ProfessorsReader.saveProfessors(activityRef.get().professors, outputStream);
                } catch (IOException e) {
                    // TODO: inform user?
                }
                activityRef.get().showActualizationInfo(
                        activityRef.get().getString(R.string.add_prof_success_title),
                        activityRef.get().getString(R.string.add_prof_success_message, professor.toString())
                );
            }
        }

        @Override
        public void onError(String errorMessage) {
            activityRef.get().showActualizationInfo(
                    activityRef.get().getString(R.string.add_prof_failure_title),
                    errorMessage
            );
        }
    }
}
