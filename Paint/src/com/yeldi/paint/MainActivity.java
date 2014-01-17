package com.yeldi.paint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ImageView background, pcolor, brush, eraser, undo, share;
	RelativeLayout mDrawingPad;
	static Paint mPaint, paint;
	static Bitmap mBitmap, bitmap;
	static Path mPath, path;
	static Canvas mCanvas, canvas;
	DrawingBrush mDrawingBrush;
	static int Randomcolor, backcolor, Paintcolor = Color.BLACK, prodialog;
    int count=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final DrawingView mDrawingView = new DrawingView(this);

		setContentView(R.layout.activity_main);

		mDrawingPad = (RelativeLayout) findViewById(R.id.relative1);

		mDrawingPad.addView(mDrawingView);

		background = (ImageView) findViewById(R.id.background);
		pcolor = (ImageView) findViewById(R.id.paint);
		brush = (ImageView) findViewById(R.id.brush);
		eraser = (ImageView) findViewById(R.id.eraser);
		undo = (ImageView) findViewById(R.id.undo);
		share = (ImageView) findViewById(R.id.share);

		background.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				BgcolorPicker();
				mCanvas.drawColor(0, Mode.CLEAR);
				mPaint.setStrokeWidth(prodialog);
				mPaint.setColor(Paintcolor);

			}
		});
		pcolor.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {

				PaintcolorPicker();
				mPaint.setStrokeWidth(prodialog);

			}
		});
		brush.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawingBrush = new DrawingBrush(MainActivity.this);
				mPaint.setStrokeWidth(prodialog);
				/*
				 * mPaint.setPathEffect(new DashPathEffect(new float[] { 2, 10
				 * }, 0));
				 */
				ShowDialogBrush();
			}
		});
		eraser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowDialog();
				if (backcolor == Color.WHITE) {
					Log.v("if", "In if");
					mPaint.setStrokeWidth(10);
					Paintcolor = Color.WHITE;
					mPaint.setColor(Paintcolor);
				}

			}
		});
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap icon = mBitmap;
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("image/jpeg");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
				try {
				    f.createNewFile();
				    FileOutputStream fo = new FileOutputStream(f);
				    fo.write(bytes.toByteArray());
				} catch (IOException e) {                       
				        e.printStackTrace();
				}
				share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
				startActivity(Intent.createChooser(share, "Share Image"));
			}
			/*String pathofBmp = Images.Media.insertImage(getContentResolver(), mBitmap,"title", null);
		    Uri bmpUri = Uri.parse(pathofBmp);
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
			shareIntent.setType("image/jpeg");
			startActivity(Intent.createChooser(shareIntent,"Share"));*/
	
				 /*File folder = new File(Environment.getExternalStorageDirectory().toString());
                 boolean success = false;
                 if (!folder.exists()) 
                 {
                     success = folder.mkdirs();
                 }

                 System.out.println(success+"folder");

                 File file = new File(Environment.getExternalStorageDirectory().toString() + "/sample.JPEG");

             if ( !file.exists() )
             {
                   try {
                    success = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
             }

             System.out.println(success+"file");
*/
            

            /* FileOutputStream ostream = null;
                try
                {
                ostream = new FileOutputStream(file);

                System.out.println(ostream);

                Bitmap save = DrawingView.getBitmap();
                if(save == null) {
                    System.out.println("NULL bitmap save\n");
                }
                save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                //bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                   ostream.flush();
                    ostream.close();
                }catch (NullPointerException e) 
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Null error", Toast.LENGTH_SHORT).show();
                }

                catch (FileNotFoundException e) 
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_SHORT).show();
                }

                catch (IOException e) 
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "IO error", Toast.LENGTH_SHORT).show();
                }*/


			
		});

	}

	protected void BgcolorPicker() {
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, backcolor,
				new OnAmbilWarnaListener() {                  

					// Executes, when user click Cancel button
					@Override
					public void onCancel(AmbilWarnaDialog dialog) {

					}

					@Override
					public void onOk(AmbilWarnaDialog dialog, int bgcolor) {

						mDrawingPad.setBackgroundColor(bgcolor);
						backcolor = bgcolor;

					}
				});
		dialog.show();

	}

	protected void PaintcolorPicker() {
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Paintcolor,
				new OnAmbilWarnaListener() {

					// Executes, when user click Cancel button
					@Override
					public void onCancel(AmbilWarnaDialog dialog) {

					}

					@Override
					public void onOk(AmbilWarnaDialog dialog, int color) {

						mPaint.setColor(color);
						Paintcolor = color;

					}
				});
		dialog.show();

	}

	public void ShowDialog() {
		final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);

		final SeekBar seek = new SeekBar(this);
		seek.setThumb(getResources().getDrawable(R.drawable.eraser));
		seek.setMax(50);
		seek.setPadding(30, 50, 20, 30);
		popDialog.setView(seek);

		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				prodialog = progress;
				mPaint.setStrokeWidth(progress);
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});

		// Button OK
		popDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (backcolor == 0) {
							Log.v("if", "In if");
							mPaint.setStrokeWidth(20);
							Paintcolor = Color.WHITE;
							mPaint.setColor(Paintcolor);

						} else {
							Log.v("else", "In else");
							mPaint.setStrokeWidth(20);
							mPaint.setColor(backcolor);
							mPath.reset();

						}

						mPaint.setStrokeWidth(prodialog);
						dialog.dismiss();
					}

				});
		popDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPaint.setColor(Paintcolor);
						dialog.dismiss();

					}

				});
		popDialog.setNeutralButton("Clear All",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						mBitmap.eraseColor(backcolor);
						mPaint.setColor(Paintcolor);
						
					
						
					}
				});
		
		popDialog.create();
		popDialog.show();

	}

	public void ShowDialogBrush() {

		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.brush_alert);

		RelativeLayout rl = (RelativeLayout) dialog
				.findViewById(R.id.br_alert_rl);
		rl.addView(mDrawingBrush);
		RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radioGroup1);
		final CheckBox chBox = (CheckBox) dialog.findViewById(R.id.random);
		// RadioGroup g = (RadioGroup) findViewById(R.id.rBtnDigits);
		/*
		 * rg.setOnCheckedChangeListener(new
		 * RadioGroup.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(RadioGroup group, int
		 * checkedId) { switch (checkedId) { case R.id.emboss:
		 * mPaint.setMaskFilter(new EmbossMaskFilter(new float[] { 1, 1, 1 },
		 * 0.4f, 10, 8.2f)); break; case R.id.normal:
		 * 
		 * break; case R.id.spray:
		 * 
		 * break; }
		 * 
		 * } });
		 */

		/*
		 * switch (rg.getCheckedRadioButtonId()) { case R.id.emboss:
		 * mPaint.setMaskFilter(new EmbossMaskFilter(new float[] { 1, 1, 1 },
		 * 0.4f, 10, 8.2f)); break;
		 * 
		 * case R.id.normal: mPaint.setStrokeWidth(10);
		 * 
		 * break; }
		 */
		/*
		 * rg.setOnCheckedChangeListener(new OnCheckedChangeListener() { public
		 * void onCheckedChanged(RadioGroup group, int checkedId) { switch
		 * (checkedId) { case R.id.emboss:
		 * 
		 * break; case R.id.normal: mPaint.setStrokeWidth(10); break; } }
		 * 
		 * });
		 */

		Button ok = (Button) dialog.findViewById(R.id.ok_btn);
		Button cancel = (Button) dialog.findViewById(R.id.cancel_btn);
		final TextView tv = (TextView) dialog.findViewById(R.id.textView1);

		final SeekBar seek = (SeekBar) dialog.findViewById(R.id.seekBar1);

		// prodialog = seek.getProgress();
		seek.setProgress(prodialog);
		seek.setMax(50);

		tv.setText("Brush Size:" + prodialog);
		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				prodialog = seek.getProgress();
				tv.setText("Brush Size:" + prodialog);
				paint.setStrokeWidth(prodialog);

			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});
		chBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton cb, boolean isChecked) {

				if (cb.isChecked()) {

					Log.v("chechbox", "chechbox");

					cb.setChecked(true);
					mPaint.setStrokeWidth(10);
					mPaint.setColor(Randomcolor);

				} else {
					
				}
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPaint.setStrokeWidth(prodialog);
				paint.setColor(Paintcolor);
				paint.setStrokeWidth(prodialog);
				dialog.dismiss();

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	static class DrawingView extends View {

		// MaskFilter mEmboss;
		// MaskFilter mBlur;

		// Canvas mCanvas;
		// Path mPath;
		Paint mBitmapPaint;

		public DrawingView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub

			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setDither(true);
			// mPaint.setColor(Color.BLACK);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(10);

			mPath = new Path();
			mBitmapPaint = new Paint();
			mBitmapPaint.setColor(Color.RED);
			// mPaint.setColor(Randomcolor);

		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

			mCanvas = new Canvas(mBitmap);
			// mPaint.setColor(Randomcolor);

			// mCanvas.drawColor(Color.WHITE);
		}

		@Override
		public void draw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.draw(canvas);
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
			Random rnd = new Random();
			Randomcolor = Color.argb(255, rnd.nextInt(260), rnd.nextInt(270),
					rnd.nextInt(280));

			/*
			 * mPaint.setShader(new LinearGradient(0, 0, 0, getHeight(),
			 * Paintcolor, Color.WHITE, Shader.TileMode.CLAMP));
			 */
			canvas.drawPath(mPath, mPaint);
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			// mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// mPaint.setXfermode(new
			// PorterDuffXfermode(PorterDuff.Mode.SCREEN));
			// kill this so we don't double draw
			mPath.reset();
			// mPath= new Path();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				// mPaint.setColor(Randomcolor);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				// mPaint.setColor(Randomcolor);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				// mPaint.setColor(Randomcolor);
				invalidate();
				break;
			}
			return true;
		}
		public static Bitmap getBitmap()
	    {
	        //this.measure(100, 100);
	        //this.layout(0, 0, 100, 100);
	        /*this.setDrawingCacheEnabled(true);  
	        this.buildDrawingCache();
	       Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());   
	        this.setDrawingCacheEnabled(false);
	*/

	    return mBitmap;
	    }


	}

	static class DrawingBrush extends View {

		Paint bitmapPaint;

		public DrawingBrush(Context context) {
			super(context);
			// TODO Auto-generated constructor stub

			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setDither(true);
			// mPaint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeWidth(10);
			paint.setColor(Paintcolor);
			path = new Path();
			bitmapPaint = new Paint();
			bitmapPaint.setColor(Color.RED);

		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

			canvas = new Canvas(bitmap);

		}

		@Override
		public void draw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.draw(canvas);
			canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);

			/*
			 * mPaint.setShader(new LinearGradient(0, 0, 0, getHeight(),
			 * Paintcolor, Color.WHITE, Shader.TileMode.CLAMP));
			 */
			canvas.drawPath(path, paint);
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			// mPath.reset();
			path.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			path.lineTo(mX, mY);
			canvas.drawPath(path, paint);
			canvas.drawColor(0, Mode.CLEAR);
			path.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				mPaint.setColor(Randomcolor);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				mPaint.setColor(Randomcolor);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();

				invalidate();
				break;
			}
			return true;
		}

	}

	/*
	 * public void onResume() {
	 * 
	 * if (backcolor == 0) { Log.v("if", "In if"); mPaint.setStrokeWidth(20);
	 * mPaint.setColor(Color.WHITE); } // mCanvas.drawColor(0, Mode.CLEAR);
	 * super.onResume(); }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
