package il.co.ilrd.VendingMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import il.co.ilrd.VendingMachine.VendingMachine.VendingSlots;

public class Main {

	public static void main(String[] args) {
		
		VendingMachine myMachine ;
		int input;
		Scanner scanIn = new Scanner(System.in);
		
		while(true) {			
			System.out.println("do you want to connect vending machine? 0 = yes, 1 = no");
			input = scanIn.nextInt();
			if(input == 0) {
				VendingMachine.VendingSlots slot1 = new VendingMachine.VendingSlots(5.00, 5);
				VendingMachine.VendingSlots slot2 = new VendingMachine.VendingSlots(7.00, 5);
				VendingMachine.VendingSlots slot3 = new VendingMachine.VendingSlots(6.00, 5);
				
				List<VendingMachine.VendingSlots> slots = new ArrayList<VendingMachine.VendingSlots>();
				slots.add(slot1);
				slots.add(slot2);
				slots.add(slot3);
				
				myMachine = new VendingMachine(slots);
				
				myMachine.start();
				break;
			}else {
				System.out.println("I will ask you again...");
			}
		}
	
		Main.printMenu(myMachine);
		scanIn.close();
	}
		
	
	public static void printMenu(VendingMachine myMachine)
	{
		int input;
		Scanner scanIn = new Scanner(System.in);
		
		while(myMachine.getState() != myMachine.completeState())
		{
		System.out.println("\n---choose your action---");
		System.out.println("Press 1 if you want to insert coins");
		System.out.println("Press 2 to choose your item");
		System.out.println("press 3 to get your current balance");
		System.out.println("press 4 to check if machine is empty");
		System.out.println("Press 0 to cancel");
		System.out.println("prices: water = 5, coke = 7, sprite = 6");
		
			input = scanIn.nextInt();
			
			
			switch (input) {
			case 0:
				myMachine.cancel();
				break;
			case 1:
				System.out.println("Choose amount of coins to enter");
				input = scanIn.nextInt();
				myMachine.insertCoin(input);
				break;
			case 2:
				System.out.println("Please choose your item number");
				System.out.println("1 = water, 2 = coke, 3 = sprite");
				input = scanIn.nextInt();
				myMachine.chooseItem(input);
				break;
			case 3:
				System.out.println("your balance is " + myMachine.getBalance());
				break;
			case 4:
				System.out.println(myMachine.isEmpty() == true ? "Machine is empty" : "Machine is not empty");
			default:
				break;
			}
		 }
		scanIn.close();
	
	}
}

