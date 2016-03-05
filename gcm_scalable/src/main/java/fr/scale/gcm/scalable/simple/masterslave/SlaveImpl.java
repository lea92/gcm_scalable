package fr.scale.gcm.scalable.simple.masterslave;

import java.util.List;

import javax.swing.JOptionPane;

import org.objectweb.proactive.api.PAActiveObject;

public class SlaveImpl implements Itf1{

	public SlaveImpl(){
		System.out.println("Slave");
	}
	
	public void compute(List<String> arg) {
		System.out.println("Slave compute");
        String str = "\n" + "Slave: " + this + "\n";
        str += "arg: ";
        for (int i = 0; i < arg.size(); i++) {
            str += arg.get(i);
            if (i + 1 < arg.size()) {
                str += " - ";
            }
        }
        System.err.println(str);
        //return str;
    }

	@Override
	public Integer getnb() {
		return 2;
	}
	
	
}
