package com.playdeca.controllers;

import com.playdeca.models.Users;
import com.playdeca.services.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;

@Named
@RequestScoped
public class DonationController implements Serializable {
    
    @Inject
    private UserService userService;
    
    @Inject
    private SessionController sessionController;
    
    private String selectedPackage;
    private Integer adminCoinAmount = 10; // Default to 10
    private Integer purchaseQuantity;
    private String payPalPopupScript;
      
    public String getSelectedPackage() {
        return selectedPackage;
    }
    
    public void setSelectedPackage(String selectedPackage) {
        this.selectedPackage = selectedPackage;
    }
    
    public Integer getAdminCoinAmount() {
        return adminCoinAmount;
    }
    
    public void setAdminCoinAmount(Integer adminCoinAmount) {
        this.adminCoinAmount = adminCoinAmount;
    }
    
    public Integer getPurchaseQuantity() {
        return purchaseQuantity;
    }
    
    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }
      
    public void setPayPalPopupScript(String payPalPopupScript) {
        this.payPalPopupScript = payPalPopupScript;
    }
    
    public void updateAdminCoinsDisplay() {
        if (sessionController.isLoggedIn() && sessionController.isAdmin()) {
            Users currentUser = sessionController.getCurrentUser();
            if (currentUser != null) {
                // Set current coins display value
                FacesContext.getCurrentInstance().getAttributes().put("currentAdminCoins", currentUser.getCoins().toString());
            }
        }
    }
    
    public boolean isAdmin() {
        return sessionController.isAdmin();
    }
    
    public void addCoinsToAdmin() {
        if (sessionController.isLoggedIn() && sessionController.isAdmin()) {
            Users currentUser = sessionController.getCurrentUser();
            if (currentUser != null && adminCoinAmount != null && adminCoinAmount > 0) {
                // Add coins to admin's account
                currentUser.setCoins(currentUser.getCoins() + adminCoinAmount);
                userService.update(currentUser);
                
                // Show success message
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                    "Added " + adminCoinAmount + " coin" + (adminCoinAmount == 1 ? "" : "s") + " to " + currentUser.getUsername() + "'s account"));
            }
        }
    }
    
    public String processPurchase() {
        if (sessionController.isLoggedIn()) {
            Users currentUser = sessionController.getCurrentUser();
            if (currentUser != null && selectedPackage != null) {
                if ("donator".equals(selectedPackage)) {
                    // Process donator package - deduct 7 coins and apply rank
                    int currentCoins = currentUser.getCoins();
                    if (currentCoins >= 7) {
                        currentUser.setCoins(currentCoins - 7);
                        currentUser.setUserGroup("donator");
                        userService.update(currentUser);
                        
                        FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                                "Purchased Donator package! Your rank has been updated."));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                                "You don't have enough coins. Need 7 coins, you have " + currentCoins));
                    }
                } else if ("supporter".equals(selectedPackage)) {
                    // Process supporter package - deduct 2 coins and apply rank
                    int currentCoins = currentUser.getCoins();
                    if (currentCoins >= 2) {
                        currentUser.setCoins(currentCoins - 2);
                        currentUser.setUserGroup("supporter");
                        userService.update(currentUser);
                        
                        FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                                "Purchased Supporter package! Your rank has been updated."));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                                "You don't have enough coins. Need 2 coins, you have " + currentCoins));
                    }
                }
            }
        }
        
        // Reset selection
        selectedPackage = null;
        
        // Refresh page to show updated coins
        return "purchase?faces-redirect=true";
    }
    
    public void processCoinPurchase() {
        if (sessionController.isLoggedIn()) {
            // Get coin quantity from hidden input (populated by JavaScript)
            int quantity = 1; // Default - will be updated by JS
            
            Users currentUser = sessionController.getCurrentUser();
            if (currentUser != null) {
                // Check if user is admin
                boolean isAdmin = sessionController.isAdmin();
                
                if (isAdmin) {
                    // Admins get coins immediately without PayPal
                    currentUser.setCoins(currentUser.getCoins() + quantity);
                    userService.update(currentUser);
                    
                    FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                            "Added " + quantity + " coin" + (quantity == 1 ? "" : "s") + " to your account!"));
                } else {
                    // Regular users get coins + PayPal popup for payment
                    currentUser.setCoins(currentUser.getCoins() + quantity);
                    userService.update(currentUser);
                    
                    // Create purchase record for tracking
                    // TODO: Add purchase record to database with PayPal transaction ID
                    
                    // Show success message and setup PayPal popup
                    FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                            "Added " + quantity + " coin" + (quantity == 1 ? "" : "s") + " to your account!"));
                    
                    // Set quantity for PayPal popup
                    this.setPurchaseQuantity(quantity);
                    this.openPayPalPopup(quantity);
                }
            }
        }
    }
    
    public void openPayPalPopup(int quantity) {
        double total = quantity * 1.99;
        String paypalUrl = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=monster_what%40hotmail.com&item_name=PlayDeca+Server+Coins+%28" + quantity + "%29&currency_code=USD&amount=" + total + "&source=url";
        
        // Open PayPal in popup window
        String script = "window.open('" + paypalUrl + "', 'paypal', 'width=600,height=400,scrollbars=yes,resizable=yes');";
        FacesContext.getCurrentInstance().getAttributes().put("paypalPopupScript", script);
    }
    
    public String getPayPalPopupScript() {
        return (String) FacesContext.getCurrentInstance().getAttributes().get("paypalPopupScript");
    }
    
    public String redirectForCoins() {
        if (sessionController.isLoggedIn()) {
            return null; // Stay on page
        } else {
            return "login?faces-redirect=true"; // Redirect to login
        }
    }
    
    public String giveAdminCoins() {
        System.out.println("giveAdminCoins called with adminCoinAmount: " + adminCoinAmount);
        if (sessionController.isLoggedIn() && sessionController.isAdmin()) {
            Users currentUser = sessionController.getCurrentUser();
            if (currentUser != null && adminCoinAmount != null && adminCoinAmount > 0) {
                // Add coins to admin's account
                int currentCoins = currentUser.getCoins();
                int newCoins = currentCoins + adminCoinAmount;
                currentUser.setCoins(newCoins);
                userService.update(currentUser);
                
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                        "Added " + adminCoinAmount + " coin" + (adminCoinAmount == 1 ? "" : "s") + " to " + currentUser.getUsername() + "'s account. Total: " + newCoins));
                
                // Reset to default after adding
                this.adminCoinAmount = 10;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                        "Please enter a valid coin amount greater than 0. Current value: " + adminCoinAmount));
            }
        }
        return "purchase?faces-redirect=true";
    }
}