package io.github.lumine1909.blocktuner.util;

import io.github.lumine1909.blocktuner.object.Note;

public class NoteUtil {

    public static String getName(Note note) {
        return note.getNote() + ", " + getName(note.getNote(), 0);
    }

    public static Note byName(String name) {
        try {
            return Note.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Note byId(String id) {
        try {
            return byNote(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Note byNote(int note) {
        return Note.NOTE_MAP.get(note);
    }

    public static String getName(int note, int keySignature) {
        String noteName = "";
        switch (note % 12) {
            case 0 -> {
                if (keySignature >= -1) {
                    noteName = "F♯$";
                } else if (keySignature <= -3) {
                    noteName = "G♭$";
                } else {
                    noteName = "F♯$ | G♭$";
                }
            }
            case 1 -> {
                if (keySignature >= 6) {
                    noteName = "F\ud834\udd2a$";
                } else if (keySignature <= 4 && keySignature >= -6) {
                    noteName = "G$";
                    if (keySignature >= 3 || keySignature <= -5) {
                        noteName = "G♮$";
                    }
                } else if (keySignature == 5) {
                    noteName = "F\ud834\udd2a$ | G♮$";
                } else {
                    noteName = "G♮$ | A\ud834\udd2b$";
                }
            }
            case 2 -> {
                if (keySignature >= 1) {
                    noteName = "G♯$";
                } else if (keySignature <= -1) {
                    noteName = "A♭$";
                } else {
                    noteName = "G♯$ | A♭$";
                }
            }
            case 3 -> {
                if (keySignature >= 7) {
                    noteName = "G\ud834\udd2a$ | A♮$";
                } else if (keySignature >= -4) {
                    noteName = "A$";
                    if (keySignature <= -3 || keySignature >= 5) {
                        noteName = "A♮$";
                    }
                } else if (keySignature <= -6) {
                    noteName = "B\ud834\udd2b$";
                } else {
                    noteName = "A♮ | B\ud834\udd2b$";
                }
            }
            case 4 -> {
                if (keySignature >= 3) {
                    noteName = "A♯$";
                } else if (keySignature <= 1) {
                    noteName = "B♭$";
                } else {
                    noteName = "A♯$ | B♭$";
                }
            }
            case 5 -> {
                if (keySignature >= -2) {
                    noteName = "B$";
                    if (keySignature <= -1 || keySignature >= 7) {
                        noteName = "B♮$";
                    }
                } else if (keySignature <= -4) {
                    noteName = "C♭$";
                } else {
                    noteName = "B♮$ | C♭$";
                }
            }
            case 6 -> {
                if (keySignature >= 5) {
                    noteName = "B♯$";
                } else if (keySignature <= 3) {
                    noteName = "C$";
                    if (keySignature >= 2 || keySignature <= -6) {
                        noteName = "C♮$";
                    }
                } else {
                    noteName = "B♯$ | C♮$";
                }
            }
            case 7 -> {
                if (keySignature >= 0) {
                    noteName = "C♯$";
                } else if (keySignature <= -2) {
                    noteName = "D♭$";
                } else {
                    noteName = "C♯$ | D♭$";
                }
            }
            case 8 -> {
                if (keySignature >= 7) {
                    noteName = "C\ud834\udd2a$";
                } else if (keySignature >= -5 && keySignature <= 5) {
                    noteName = "D$";
                    if (keySignature <= -4 || keySignature >= 4) {
                        noteName = "D♮$";
                    }
                } else if (keySignature <= -7) {
                    noteName = "E\ud834\udd2b$";
                } else if (keySignature == -6) {
                    noteName = "C\ud834\udd2a$ | D♮$";
                } else {
                    noteName = "D♮ | E\ud834\udd2b$";
                }
            }
            case 9 -> {
                if (keySignature >= 2) {
                    noteName = "D♯$";
                } else if (keySignature <= 0) {
                    noteName = "E♭$";
                } else {
                    noteName = "D♯$ | E♭$";
                }
            }
            case 10 -> {
                if (keySignature >= -3) {
                    noteName = "E$";
                    if (keySignature <= -2 || keySignature >= 6) {
                        noteName = "E♮$";
                    }
                } else if (keySignature <= -5) {
                    noteName = "F♭$";
                } else {
                    noteName = "E♮$ | F♭$";
                }
            }
            case 11 -> {
                if (keySignature >= 4) {
                    noteName = "E♯$";
                } else if (keySignature <= 2) {
                    noteName = "F$";
                    if (keySignature >= 1 || keySignature <= -7) {
                        noteName = "F♮$";
                    }
                } else {
                    noteName = "E♯$ | F♮$";
                }
            }
        }
        if (note >= 18) {
            noteName = noteName.replaceAll("\\$", "+");
        } else if (note >= 6) {
            noteName = noteName.replaceAll("\\$", "");
        } else {
            noteName = noteName.replaceAll("\\$", "-");
        }
        return noteName;
    }
}