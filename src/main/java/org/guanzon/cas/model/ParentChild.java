/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.model;

/**
 *
 * @author User
 */
public class ParentChild {
    private boolean isChildFormOpen = false;
    private boolean isParent = false;

    public void openChildForm() {
        // Open the child form
        isChildFormOpen = true;
        isParent = false;
        // Code to open the child form
    }

    public void closeChildForm() {
        // Close the child form
        isChildFormOpen = false;
        // Code to close the child form
    }

    public boolean isChildFormOpen() {
        // Check if the child form is open
        return isChildFormOpen;
    }
    
}
