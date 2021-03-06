# Render-Experiments
I wanted to learn how to render graphics and build my own graphics engine in my freetime. This was an example from Chapter 2 of my textbook. The image below is the result. I use the OpenGL libraries to do this. The triangle is rendered in real time with a shader. The triangle moves across the screen and changes colors as it does so

Right triangle moves back and forth horizontally across the screen and changes colors according to shader algorithm

![alt tag](https://github.com/austings/Render-Experiments/blob/master/pics/preview.png)

A cube growing and shrinking indefinitely

![alt tag](https://github.com/austings/Render-Experiments/blob/master/pics/preview2.png)

An isococles triangle

![alt tag](https://github.com/austings/Render-Experiments/blob/master/pics/preview3.png)


Shaders are compiled using OpenGL for Java. Below is the exerpt that builds the shaders out

```
//Create shader program. Initializes shading properties for an item
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
		String vshaderSource[ ] = glr.readShaderSource("./resource/shaders/vertexshadertri.vs");
		String fshaderSource[ ] = glr.readShaderSource("./resource/shaders/fragmentshader.fs");

		
		//vertex shader
		int vShader = gl.glCreateShader(GL_VERTEX_SHADER);//create shader
		gl.glShaderSource(vShader, 6, vshaderSource, null, 0); //set source
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


