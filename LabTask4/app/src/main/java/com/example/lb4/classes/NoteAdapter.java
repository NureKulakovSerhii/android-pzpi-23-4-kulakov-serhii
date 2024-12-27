package com.example.lb4.classes;

import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lb4.R;
import com.example.lb4.classes.Note;

import java.text.SimpleDateFormat;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private OnNoteClickListener listener;
    private int longClickedPosition = -1;

    public NoteAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteTitle.setText(note.GetTitle());
        holder.noteDescription.setText(note.GeDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        holder.noteDate.setText(sdf.format(note.GetDate()));

        switch (note.GetPriority()) {
            case 1:
                holder.priorityIcon.setImageResource(R.drawable.level1);
                break;
            case 2:
                holder.priorityIcon.setImageResource(R.drawable.level2);
                break;
            case 3:
                holder.priorityIcon.setImageResource(R.drawable.level3);
                break;
            default:
                holder.priorityIcon.setImageResource(R.drawable.level1);
                break;
        }
        if (note.GetPhotoPath() != null && !note.GetPhotoPath().isEmpty()) {
            Uri imageUri = Uri.parse(note.GetPhotoPath());
            holder.noteImage.setImageURI(imageUri);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickedPosition = holder.getAdapterPosition();
                return false;
            }
        });

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE, 1, Menu.NONE, "Редагувати");
                menu.add(Menu.NONE, 2, Menu.NONE, "Видалити");
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public int getLongClickedPosition() {
        return longClickedPosition;
    }

    public void updateNotes(List<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle;
        TextView noteDescription;
        TextView noteDate;
        ImageView priorityIcon;
        ImageView noteImage;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteDescription = itemView.findViewById(R.id.noteDescription);
            noteDate = itemView.findViewById(R.id.noteDate);
            priorityIcon = itemView.findViewById(R.id.priorityIcon);
            noteImage = itemView.findViewById(R.id.noteImage);
        }
    }
    public interface OnNoteClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }
}