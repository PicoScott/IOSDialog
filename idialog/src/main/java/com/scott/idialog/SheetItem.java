package com.scott.idialog;

public final class SheetItem {
        public static final int RED = 0xFFFD4A2E;
        public static final int BLUE = 0xFF037BFF;
        //public static final int GREY = Color.parseColor("#FFFD4A2E");

        public String name;
        public int color = -1;
        public float textSize = -1;

        public SheetItem() {
        }

        public SheetItem(String name) {
            this.name = name;
        }

        public SheetItem(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public SheetItem(String name, int color, float textSize) {
            this.name = name;
            this.color = color;
            this.textSize = textSize;
        }

    }