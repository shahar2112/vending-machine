package il.co.ilrd.VendingMachine;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class VendingMachine {
	
	private VendingState state;
	private List<VendingSlots> items;
	private double balance;

	private enum VendingState implements view 
	{
		INIT{
			@Override
			void insertCoin(VendingMachine vm, double coin) {}
			
			@Override
			void chooseItem(VendingMachine vm, int item_num) {}
			
			@Override
			void timeOut(VendingMachine vm) {}

			@Override
			void cancel(VendingMachine vm) {}
		},
		IDLE{		
			@Override
			void insertCoin(VendingMachine vm, double coin) {
				vm.balance += coin;
				changeState(vm, COLLECTING_MONEY);
				vm.state.timeOut(vm);
			}
			
			@Override
			void chooseItem(VendingMachine vm, int item_num) {
				print("Please first insert money");
			}
			
			@Override
			void timeOut(VendingMachine vm) {}

			@Override
			void cancel(VendingMachine vm) {
				print("Plese choose another option");
			}
		},
		COLLECTING_MONEY {
			@Override
			void cancel(VendingMachine vm) {
				print("Canceling");
				thread.interrupt();
				vm.balance = 0;
				changeState(vm, IDLE);
			}
			
			@Override
			void insertCoin(VendingMachine vm, double coin) {
				thread.interrupt();
				vm.state.timeOut(vm);
				vm.balance += coin;
			}
			
			@Override
			void chooseItem(VendingMachine vm, int item_num) {
				double price = vm.items.get(item_num -1).price;
				if(price > vm.balance) {
					print("Not enough money, please insert more");
					thread.interrupt();
					vm.state.timeOut(vm);
				}
				else {
					print("\nThank you, don't forget your change :)");
					vm.balance = 0;
					vm.items.get(item_num -1).numOfProducts--;
					changeState(vm, VENDING_COMPLETE);
				}
			}
			
			@Override
			void timeOut(VendingMachine vm) {
				
				timer = vm.new Timeout();
				thread = new Thread(timer);
				thread.start();

			}
		},
		
		VENDING_COMPLETE{
			@Override
			void cancel(VendingMachine vm) {}
			
			@Override
			void insertCoin(VendingMachine vm, double coin) {}
			
			@Override
			void chooseItem(VendingMachine vm, int item_num) {
				print("Please ");
				
			}
			
			@Override
			void timeOut(VendingMachine vm) {
				changeState(vm, IDLE);			
			}

		};
		
		@Override
		public void print(String str) {
			System.out.println(str);
		}
		
		void changeState(VendingMachine vm, VendingState state) {
			vm.state = state;
		}
		
		VendingMachine.Timeout timer;
		Thread thread;
		
		abstract void cancel(VendingMachine vm);
		abstract void insertCoin(VendingMachine vm, double coin);
		abstract void chooseItem(VendingMachine vm, int item_num);
		abstract void timeOut(VendingMachine vm);
	}
	
	//constructor
	public VendingMachine(List<VendingSlots> items) {
		this.items = items;
		this.state = VendingState.INIT;
		this.balance = 0;
	}

	
	public void start() {
		state.changeState(this, VendingState.IDLE);
		state.print("Ready to start...");
	}
	
	
	public void insertCoin(double amount) {
		state.insertCoin(this, amount);	
	}
	
	public void chooseItem(int code) {
		if((code > items.size()) || (code < 1)) {
			System.out.println("Item doesn't exist, please try again");
			return;
		}
		state.chooseItem(this, code);
	}
	
	public boolean isEmpty() {
		for(VendingSlots slots : items) {
			if(slots.numOfProducts != 0) {
				return false;
			}
		}
		return true;
	}
	
	public double getBalance() {
		return balance;
	}
	
	
	public void cancel() {
		state.cancel(this);
	}
	
	public VendingState getState() {
		return this.state;
	}
	
	public VendingState completeState() {
		return VendingState.VENDING_COMPLETE;
	}
	
	public static class VendingSlots {
		private double price;
		private int numOfProducts;
		
		public VendingSlots(double price, int numOfProducts) {
			this.price = price;
			this.numOfProducts = numOfProducts;
		}
		
	}
	
	
	
	class Timeout implements Runnable, view {
		private boolean to_Stop;
		
		public Timeout() {
			to_Stop = false;
		}
		    
		@Override
		public void run() {

	            try {
	            	TimeUnit.SECONDS.sleep(15);
	            	to_Stop = true;
	            } catch (InterruptedException e) {

	            }
	      if(to_Stop == true) {
	    	  balance = 0;
	    	  print("RESTARTING MACHINE...");
	    	  state.changeState(VendingMachine.this, VendingState.IDLE);	  
	      }
	  }
		
		
		
		@Override
		public void print(String str) {
			System.out.println(str);
		}

	}

}
