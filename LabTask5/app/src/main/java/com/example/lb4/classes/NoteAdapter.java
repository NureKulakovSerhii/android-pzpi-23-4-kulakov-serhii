package com.example.lb4.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
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

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private OnNoteClickListener listener;
    private boolean isDarkTheme;
    private int longClickedPosition = -1;

    public NoteAdapter(List<Note> notes, OnNoteClickListener listener, boolean isDarkTheme) {
        this.notes = notes;
        this.listener = listener;
        this.isDarkTheme = isDarkTheme;
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
            try {
                File imageFile = new File(note.GetPhotoPath());
                if (imageFile.exists()) {
                    Uri imageUri = Uri.fromFile(imageFile);
                    holder.noteImage.setImageURI(imageUri);
                } else {
                    holder.noteImage.setImageResource(android.R.color.transparent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.noteImage.setImageResource(android.R.color.transparent);
            }
        } else {
            holder.noteImage.setImageResource(android.R.color.transparent);
        }
        if (isDarkTheme) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.note_background_dark));
            holder.noteTitle.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.dark_textColor));
            holder.noteDescription.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.dark_textColor));
            holder.noteDate.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.dark_textColor));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            holder.noteTitle.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
            holder.noteDescription.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
            holder.noteDate.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
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
                menu.add(Menu.NONE, 1, Menu.NONE, "Редагувати").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (listener != null) {
                            listener.onEditClick(holder.getAdapterPosition());
                        }
                        return true;
                    }
                });
                menu.add(Menu.NONE, 2, Menu.NONE, "Видалити").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (listener != null) {
                            listener.onDeleteClick(holder.getAdapterPosition());
                        }
                        return true;
                    }
                });
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
        this.notes.clear();
        this.notes.addAll(newNotes);
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
    public Note getNoteAtPosition(int position) {
        return notes.get(position);
    }
    public void updateTheme(boolean isDarkTheme) {
        this.isDarkTheme = isDarkTheme;
        notifyDataSetChanged();
    }
    public interface OnNoteClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

}