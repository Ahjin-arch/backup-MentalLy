package com.example.myaply.ui.relax;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.example.myaply.R;

public class BuscaminasBottomSheet extends BottomSheetDialogFragment {

    private Tablero fondo;
    private Casilla[][] casillas;
    private boolean activo = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_buscaminas, container, false);

        LinearLayout linearLayout = view.findViewById(R.id.layout1);
        fondo = new Tablero(requireContext());
        linearLayout.addView(fondo);

        fondo.setOnTouchListener((v, event) -> onTouch(v, event));
        Button btnReiniciar = view.findViewById(R.id.btnReiniciar);
        btnReiniciar.setOnClickListener(v -> reiniciar());

        reiniciar();
        return view;
    }

    public void reiniciar() {
        casillas = new Casilla[8][8];
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                casillas[f][c] = new Casilla();
            }
        }
        this.disponerBombas();
        this.contarBombasPerimetro();
        activo = true;
        fondo.invalidate();
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && activo) {
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    if (casillas[f][c].dentro((int) event.getX(), (int) event.getY())) {
                        casillas[f][c].destapado = true;
                        if (casillas[f][c].contenido == 80) {
                            Toast.makeText(requireContext(), "Boooooom!", Toast.LENGTH_LONG).show();
                            activo = false;
                            mostrarTodasBombas();
                        } else if (casillas[f][c].contenido == 0) {
                            recorrer(f, c);
                        }
                        fondo.invalidate();
                    }
                }
            }

            if (gano() && activo) {
                Toast.makeText(requireContext(), "Ganaste!", Toast.LENGTH_LONG).show();
                activo = false;
            }
        }
        return true;
    }

    private void mostrarTodasBombas() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (casillas[f][c].contenido == 80) {
                    casillas[f][c].destapado = true;
                }
            }
        }
        fondo.invalidate();
    }

    private void disponerBombas() {
        int cantidad = 8;
        do {
            int fila = (int) (Math.random() * 8);
            int columna = (int) (Math.random() * 8);
            if (casillas[fila][columna].contenido == 0) {
                casillas[fila][columna].contenido = 80;
                cantidad--;
            }
        } while (cantidad != 0);
    }

    private boolean gano() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (!casillas[f][c].destapado && casillas[f][c].contenido != 80) {
                    return false;
                }
            }
        }
        return true;
    }

    private void contarBombasPerimetro() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (casillas[f][c].contenido == 0) {
                    int cant = contarCoordenada(f, c);
                    casillas[f][c].contenido = cant;
                }
            }
        }
    }

    int contarCoordenada(int fila, int columna) {
        int total = 0;
        if (fila - 1 >= 0 && columna - 1 >= 0) {
            if (casillas[fila - 1][columna - 1].contenido == 80) total++;
        }
        if (fila - 1 >= 0) {
            if (casillas[fila - 1][columna].contenido == 80) total++;
        }
        if (fila - 1 >= 0 && columna + 1 < 8) {
            if (casillas[fila - 1][columna + 1].contenido == 80) total++;
        }
        if (columna + 1 < 8) {
            if (casillas[fila][columna + 1].contenido == 80) total++;
        }
        if (fila + 1 < 8 && columna + 1 < 8) {
            if (casillas[fila + 1][columna + 1].contenido == 80) total++;
        }
        if (fila + 1 < 8) {
            if (casillas[fila + 1][columna].contenido == 80) total++;
        }
        if (fila + 1 < 8 && columna - 1 >= 0) {
            if (casillas[fila + 1][columna - 1].contenido == 80) total++;
        }
        if (columna - 1 >= 0) {
            if (casillas[fila][columna - 1].contenido == 80) total++;
        }
        return total;
    }

    private void recorrer(int fil, int col) {
        if (fil >= 0 && fil < 8 && col >= 0 && col < 8) {
            if (casillas[fil][col].contenido == 0) {
                casillas[fil][col].destapado = true;
                casillas[fil][col].contenido = 50;
                recorrer(fil, col + 1);
                recorrer(fil, col - 1);
                recorrer(fil + 1, col);
                recorrer(fil - 1, col);
                recorrer(fil - 1, col - 1);
                recorrer(fil - 1, col + 1);
                recorrer(fil + 1, col + 1);
                recorrer(fil + 1, col - 1);
            } else if (casillas[fil][col].contenido >= 1 && casillas[fil][col].contenido <= 8) {
                casillas[fil][col].destapado = true;
            }
        }
    }

    class Tablero extends View {
        public Tablero(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int size = Math.min(width, height);
            setMeasuredDimension(size, size);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(0, 0, 0);
            int ancho = Math.min(getWidth(), getHeight());
            int anchocua = ancho / 8;
            Paint paint = new Paint();
            paint.setTextSize(20);
            Paint paint2 = new Paint();
            paint2.setTextSize(20);
            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            paint2.setARGB(255, 0, 0, 255);
            Paint paintlinea1 = new Paint();
            paintlinea1.setARGB(255, 255, 255, 255);
            int filaact = 0;
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    casillas[f][c].fijarxy(c * anchocua, filaact, anchocua);
                    if (!casillas[f][c].destapado)
                        paint.setARGB(153, 204, 204, 204);
                    else
                        paint.setARGB(255, 153, 153, 153);
                    canvas.drawRect(c * anchocua, filaact, c * anchocua + anchocua - 2, filaact + anchocua - 2, paint);
                    canvas.drawLine(c * anchocua, filaact, c * anchocua + anchocua, filaact, paintlinea1);
                    canvas.drawLine(c * anchocua + anchocua - 1, filaact, c * anchocua + anchocua - 1, filaact + anchocua, paintlinea1);
                    if (casillas[f][c].contenido >= 1 && casillas[f][c].contenido <= 8 && casillas[f][c].destapado)
                        canvas.drawText(String.valueOf(casillas[f][c].contenido), c * anchocua + (anchocua / 2) - 8, filaact + anchocua / 2, paint2);
                    if (casillas[f][c].contenido == 80 && casillas[f][c].destapado) {
                        Paint bomba = new Paint();
                        bomba.setARGB(255, 255, 0, 0);
                        canvas.drawCircle(c * anchocua + (anchocua / 2), filaact + (anchocua / 2), 8, bomba);
                    }
                }
                filaact = filaact + anchocua;
            }
        }
    }

    class Casilla {
        int contenido = 0;
        boolean destapado = false;
        int x, y, ancho;

        public void fijarxy(int x, int y, int ancho) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
        }

        public boolean dentro(int xp, int yp) {
            return xp >= x && xp <= x + ancho && yp >= y && yp <= y + ancho;
        }
    }



}