package joglTestProjtest;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.nio.*;
import javax.swing.*;

import static com.jogamp.opengl.GL2ES3.GL_COLOR;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.common.nio.Buffers;

public class Display extends JFrame implements GLEventListener
{
	
	private GLCanvas myCanvas;
	private int rendering_program;
	private int vao[ ] = new int[1];
	
	private float x = 0.0f;
	private float inc = 0.01f;
	
	public Display()
	{ 
		setTitle("Chapter2 - program1");
		setSize(600, 400);
		setLocation(200, 200);
		myCanvas = new GLCanvas();
		myCanvas.addGLEventListener(this);
		this.add(myCanvas);
		setVisible(true);
		FPSAnimator animtr = new FPSAnimator(myCanvas, 60);
		animtr.start();
	}
	
	//called after init
	public void display(GLAutoDrawable drawable)
	{ 
		GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glUseProgram(rendering_program);
		x+= inc;
		if(x>50.0f) inc= -0.05f;
		if(x<0.1f) inc= 0.05f;
		gl.glPointSize(x);
		//gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);//wireframe
		
		float bkg[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer bkgBuffer = Buffers.newDirectFloatBuffer(bkg);
		gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);
		
		//x+= inc;
		//if(x>0.75f) inc = -0.01f;
		//if(x<-0.75f) inc = 0.01f;
		//int offset_loc = gl.glGetUniformLocation(rendering_program, "offset");
		//gl.glProgramUniform1f(rendering_program, offset_loc,x);
		
		gl.glDrawArrays(GL_POINTS, 0, 1);
	}
	
	//opengl callback after gl4 object created when made visible
	//should load models/code
	//init loads our shaders and creates the intial program settings.
	//display, on the other hand, uses the initial program settings 
	//and redraws each frame. So in display we call the rendering program
	//we establish in init, and applies it to any arrays we draw.
	public void init(GLAutoDrawable drawable)
	{ 
		GL4 gl = (GL4) GLContext.getCurrentGL();
		rendering_program = createShaderProgram();
		gl.glGenVertexArrays(vao.length, vao, 0);
		gl.glBindVertexArray(vao[0]);
	}
	
	private int createShaderProgram()
	{ 
		// arrays to collect GLSL compilation status values.
		// note: one-element arrays are used because the associated JOGL calls
		//require arrays.
		int[ ] vertCompiled = new int[1];
		int[ ] fragCompiled = new int[1];
		int[ ] linked = new int[1];
		GLSLReader glr = new GLSLReader();
		
		//get shader sources
		GL4 gl = (GL4) GLContext.getCurrentGL();
		String vshaderSource[ ] = glr.readShaderSource("./resource/shaders/vertexshader.vs");
		String fshaderSource[ ] = glr.readShaderSource("./resource/shaders/fragmentshader.fs");

		
		//vertex shader
		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);//create shader
		gl.glShaderSource(vShader, 3, vshaderSource, null, 0); //set source
		gl.glCompileShader(vShader);//compile shader
		checkOpenGLError(); //error check
		gl.glGetShaderiv(vShader, GL_COMPILE_STATUS, vertCompiled,0);
		if(vertCompiled[0]==1)
			System.out.println("...vertex compilation success.");
		else {
			System.out.println("...vertex compilation failed.");
			printShaderLog(vShader);
		}

		//fragment shader
		int fShader=gl.glCreateShader(GL_FRAGMENT_SHADER);//create shader
		gl.glShaderSource(fShader, 4, fshaderSource, null, 0); //set source
		gl.glCompileShader(fShader);//compile shader
		checkOpenGLError();//error check
		gl.glGetShaderiv(fShader, GL_COMPILE_STATUS, fragCompiled,0);
		if(fragCompiled[0]==1)
			System.out.println("...fragment compilation success.");
		else
		{
			System.out.println("...fragment compilation failed.");
			printShaderLog(fShader);
		}
		
		//program linking
		int vfprogram = gl.glCreateProgram();//create link
		gl.glAttachShader(vfprogram, vShader);
		gl.glAttachShader(vfprogram, fShader);
		//catch errors while linking shaders
		gl.glLinkProgram(vfprogram);//compile link
		checkOpenGLError();//error check
		gl.glGetProgramiv(vfprogram, GL_LINK_STATUS, linked,0);
		if(linked[0]==1)
			System.out.println("...linking succeeded.");
		else
		{
			System.out.println("...linking failed.");
			printProgramLog(vfprogram);
		}
		
		//delete shaders when done.
		gl.glDeleteShader(vShader);
		gl.glDeleteShader(fShader);
	
	
	return vfprogram;
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{ 
		
	}
	
	public void dispose(GLAutoDrawable drawable)
	{ 
		
	}
	
	//
	//ERROR LOGGING METHODS
	//
	
	//int shader- pointer to the shader we want to print the log for
	private void printShaderLog(int shader)
	{
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] len = new int[1];
		int[] chWrittn = new int[1];
		byte[] log = null;
		
		//determine the length of the shader compilation log
		gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, len,0);//TODO: what are these parameters?
		if(len[0]>0)
		{
			log = new byte[len[0]];
			gl.glGetShaderInfoLog(shader, len[0],chWrittn,0, log,0);
			System.out.println("Shader Info Log: ");
			for(int i =0;i<log.length;i++)
				System.out.print((char) log[i]);
		}	
	}
	
	//takes the rendering program
	void printProgramLog(int prog)
	{
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int[ ] len = new int[1];
		int[ ] chWrittn = new int[1];
		byte[ ] log = null;
		// determine the length of the program linking log
		gl.glGetProgramiv(prog,GL_INFO_LOG_LENGTH,len, 0);
		if (len[0] > 0){ 
			log = new byte[len[0]];
			gl.glGetProgramInfoLog(prog, len[0], chWrittn, 0,log, 0);
			System.out.println("Program Info Log: ");
			for (int i = 0; i < log.length; i++)
				System.out.print((char) log[i]);
		} 
	} 
	
	boolean checkOpenGLError()
	{ 
		GL4 gl = (GL4) GLContext.getCurrentGL();
		boolean foundError = false;
		GLU glu = new GLU();
		int glErr = gl.glGetError();
		while (glErr != GL_NO_ERROR)
		{ 
			System.err.println("glError: " + glu.gluErrorString(glErr));
			foundError = true;
			glErr = gl.glGetError();
		}
		return foundError;
	}
}
