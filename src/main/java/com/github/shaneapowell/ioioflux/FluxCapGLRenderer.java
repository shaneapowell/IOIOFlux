package com.github.shaneapowell.ioioflux;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;


/**********************************************************
 *
 ***********************************************************/
public class FluxCapGLRenderer implements GLSurfaceView.Renderer
{

	private int mHeight;
	private int mWidth;
	private int mShortestHW;

	private FluxCap mFluxCap;
	private GLFluxCapPixel mPixel;


//	/**************************************************
//	 *
//	 * @param gl
//	 * @param width
//	 * @param height
//	 ***************************************************/
//	private void enable2d(GL10 gl, int width, int height)
//	{
//		gl.glMatrixMode(GL10.GL_PROJECTION);
//		gl.glPushMatrix();
//		gl.glLoadIdentity();
//		gl.glOrthof(0.0f, width, height, 0.0f, -1.0f, 1.0f);
//		gl.glViewport(0, 0, width, height);
//		gl.glMatrixMode(GL10.GL_MODELVIEW);
//		gl.glPushMatrix();
//		gl.glLoadIdentity();
//	}
//
//
//	/**************************************************
//	 *
//	 * @param gl
//	 ***************************************************/
//	private void disable2d(GL10 gl)
//	{
//		gl.glMatrixMode(GL10.GL_PROJECTION);
//		gl.glPopMatrix();
//		gl.glMatrixMode(GL10.GL_MODELVIEW);
//		gl.glPopMatrix();
//	}
//


	/**************************************************
	 *
	 * @param gl10
	 * @param config
	 ***************************************************/
	@Override
	public void onSurfaceCreated(GL10 gl10, EGLConfig config)
	{

		GL11 gl = (GL11) gl10;

		// Set the background color to black ( rgba ).
		gl.glClearColor(0.2f, 0.2f, 0.2f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		this.mPixel = new GLFluxCapPixel();

	}

	/**************************************************
	 *
	 * @param gl10
	 * @param width
	 * @param height
	 ***************************************************/
	@Override
	public void onSurfaceChanged(GL10 gl10, int width, int height)
	{
		this.mHeight = height;
		this.mWidth = width;
		this.mShortestHW = Math.min(height, width);

		GL11 gl = (GL11)gl10;
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// center 0,0 in the middle of the screen
		gl.glOrthof(-width, width, height, -height, -1.0f, 1.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();

	}

	/**************************************************
	 * Important note to those of you reading this with more GL experience than I have.
	 * I based the transforms on (0,0) being in the upper left corner of the View.
	 * Much like the rest of the standard views do.  I only found out recently that
	 * OpenGL draws elements with positive values UP.  So.. rather than mess too
	 * much with the code, instead I multiply the Y coord values by -1 when the buffer
	 * is generated.  As a result, this block of code might not make total sense at first look.
	 * @param gl10
	 **************************************************/
	@Override
	public void onDrawFrame(GL10 gl10)
	{
		GL11 gl = (GL11) gl10;
		gl.glLoadIdentity();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);


		if (this.mFluxCap != null)
		{

			float pixelSize = this.mShortestHW * 0.1f;

			gl.glScalef(pixelSize, pixelSize, pixelSize);
			gl.glTranslatef(0f, -0.5f, 0f);

			/* Bottom Arm */
			gl.glPushMatrix();
			gl.glTranslatef(0, 1.5f, 0f);
			for (int index = 0; index < mFluxCap.getArmLength(); index++)
			{
				mPixel.draw(gl10, mFluxCap.getPixel(FluxCap.ARM.BOTTOM, index).getColor());
				gl.glTranslatef(0, 2.5f, 0f);
			}
			gl.glPopMatrix();

			/* Left Arm */
			gl.glPushMatrix();
			gl.glTranslatef(-1.5f, -1.5f, 0f);
			for (int index = 0; index < mFluxCap.getArmLength(); index++)
			{
				mPixel.draw(gl10, mFluxCap.getPixel(FluxCap.ARM.LEFT, index).getColor());
				gl.glTranslatef(-2f, -2f, 0f);
			}
			gl.glPopMatrix();

			/* Right Arm */
			gl.glPushMatrix();
			gl.glTranslatef(1.5f, -1.5f, 0f);
			for (int index = 0; index < mFluxCap.getArmLength(); index++)
			{
				mPixel.draw(gl10, mFluxCap.getPixel(FluxCap.ARM.RIGHT, index).getColor());
				gl.glTranslatef(2.f, -2f, 0f);
			}
		}

		gl.glPopMatrix();
	}



	/******************************************************
	 *
	 * @param fluxCap
	 *******************************************************/
	public void setFluxCap(FluxCap fluxCap)
	{
		this.mFluxCap = fluxCap;
	}


	/*******************************************************
	 *
	 ********************************************************/
	private static class GLFluxCapPixel
	{

		private int mPoints=360;
		private float mVertices[]={0.0f,0.0f,0.0f};
		private FloatBuffer mVertBuff;


		public GLFluxCapPixel()
		{

			mVertices=new float[(mPoints+1)*3];
			for(int i=3;i<(mPoints+1)*3;i+=3)
			{
				double rad=(i*360/mPoints*3)*(3.14/180);
				mVertices[i]=(float)Math.cos(rad);
				mVertices[i+1]=(float) Math.sin(rad);
				mVertices[i+2]=0;
			}
			ByteBuffer bBuff=ByteBuffer.allocateDirect(mVertices.length*4);
			bBuff.order(ByteOrder.nativeOrder());
			mVertBuff=bBuff.asFloatBuffer();
			mVertBuff.put(mVertices);
			mVertBuff.position(0);


		}

		public void draw(GL10 gl, RGB color)
		{
			gl.glPushMatrix();
			gl.glTranslatef(0, 0, 0);
//  gl.glScalef(size, size, 1.0f);
			gl.glColor4f(color.red() / 255f, color.green() / 255f, color.blue() / 255f, 1.0f);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertBuff);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, mPoints/2);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glPopMatrix();
		}

	}




}
