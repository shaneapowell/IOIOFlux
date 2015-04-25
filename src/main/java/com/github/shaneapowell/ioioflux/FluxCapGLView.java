package com.github.shaneapowell.ioioflux;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;


/**********************************************************
 *
 ***********************************************************/
public class FluxCapGLView extends GLSurfaceView
{



	private FluxCapGLRenderer mRenderer;


	/******************************************************
	 * Don't create a new view, instead have the FluxCap object
	 * create one for you.
	 * @param context

	 *******************************************************/
	public FluxCapGLView(Context context, AttributeSet attr)
	{
		super(context, attr);
//		setBackgroundColor(Color.BLACK);
		this.mRenderer = new FluxCapGLRenderer();
//		this.setEGLContextClientVersion(2);
//		this.setPreserveEGLContextOnPause(true);
		this.setRenderer(this.mRenderer);
//		setRenderMode(RENDERMODE_WHEN_DIRTY); //TODO: Try this ease CPU load
	}


	/******************************************************
	 *
	 * @param fluxCap
	 *******************************************************/
	protected void setFluxCap(FluxCap fluxCap)
	{
		this.mRenderer.setFluxCap(fluxCap);
	}



}
