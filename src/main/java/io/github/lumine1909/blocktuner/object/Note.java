package io.github.lumine1909.blocktuner.object;

import com.google.common.collect.ImmutableMap;
import io.github.lumine1909.blocktuner.util.NoteUtil;

import java.util.Map;

public enum Note {
    F_SHARP_LOW(0),
    G_NATURAL_LOW(1),
    G_SHARP_LOW(2),
    A_NATURAL_LOW(3),
    A_SHARP_LOW(4),
    B_NATURAL_LOW(5),
    C_NATURAL_MID(6),
    C_SHARP_MID(7),
    D_NATURAL_MID(8),
    D_SHARP_MID(9),
    E_NATURAL_MID(10),
    F_NATURAL_MID(11),
    F_SHARP_MID(12),
    G_NATURAL_MID(13),
    G_SHARP_MID(14),
    A_NATURAL_MID(15),
    A_SHARP_MID(16),
    B_NATURAL_MID(17),
    C_NATURAL_HIGH(18),
    C_SHARP_HIGH(19),
    D_NATURAL_HIGH(20),
    D_SHARP_HIGH(21),
    E_NATURAL_HIGH(22),
    F_NATURAL_HIGH(23),
    F_SHARP_HIGH(24),
    EMPTY(-1);

    public static final Map<Integer, Note> NOTE_MAP;

    static {
        ImmutableMap.Builder<Integer, Note> builder = ImmutableMap.builder();
        for (Note note : Note.values()) {
            builder.put(note.getNote(), note);
        }
        NOTE_MAP = builder.build();
    }

    private final int note;

    Note(int note) {
        this.note = note;
    }

    public int getNote() {
        return note;
    }

    @Override
    public String toString() {
        return NoteUtil.getName(this);
    }
}