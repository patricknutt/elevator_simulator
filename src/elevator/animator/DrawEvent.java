/*
 * TITLE: Program 7.5
 *
 * @(#)DrawEvent 2002/07/21
 * @author Charles W. Kann III
 *
 * Copyright (c) 2002 CRC Press
 * All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this
 * copyright notice appears in all copies.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package elevator.animator;

import java.util.*;
import java.awt.*;

/**
 *  Purpose:  The DrawEvent class is the Event Source Object
 *            from the Java Event Model used in the generic
 *            animator.  It contains an extra field, the
 *            myGraphics variable, to pass the Graphics 
 *            object to draw on to the DrawListener.
 */

public class DrawEvent extends EventObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Graphics myGraphics;

    public DrawEvent(Object source, Graphics graphics) {
        super(source);
        myGraphics = graphics;
    }

    public Graphics getGraphics() {
        return myGraphics;
    }
}