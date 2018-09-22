package kasia.mimuwmap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class ProfessorArrayAdapter extends ArrayAdapter<Professor> {
    private List<Professor> professorList;
    private Filter professorFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Professor> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(professorList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Professor professor : professorList) {
                    if (professor.toString().toLowerCase().contains(filterPattern)) {
                        suggestions.add(professor);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List<Professor>) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Professor) resultValue).getFullName();
        }
    };

    public ProfessorArrayAdapter(@NonNull Context context, int resource,
                                 @NonNull List<Professor> professors) {
        super(context, resource, professors);
        professorList = new ArrayList<>(professors);
    }
}
