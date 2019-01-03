package main.view;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import static main.exceptions.Validations.numberAlert;
import static main.exceptions.Validations.productValidation;
import main.model.Inventory;
import main.model.Part;
import main.model.Product;

/**
 * FXML Controller class
 *
 * @author Drew
 */
public class ModifyProductController implements Initializable {

    @FXML
    private TextField productID;
    @FXML
    private TextField GetProductName;
    @FXML
    private TextField GetProductInv;
    @FXML
    private TextField GetProductPrice;
    @FXML
    private TextField GetProductMax;
    @FXML
    private TextField GetProductMin;
    @FXML
    private TableView<Part> AddPartTable;
    @FXML
    private TableColumn<?, ?> AddPartID;
    @FXML
    private TableColumn<?, ?> AddPartName;
    @FXML
    private TableColumn<?, ?> AddPartInv;
    @FXML
    private TableColumn<?, ?> AddPartPrice;
    @FXML
    private Button AddButton;
    @FXML
    private TableView<Part> DeletePartTable;
    @FXML
    private TableColumn<?, ?> DeletePartID;
    @FXML
    private TableColumn<?, ?> DeletePartName;
    @FXML
    private TableColumn<?, ?> DeletePartInv;
    @FXML
    private TableColumn<?, ?> DeletePartPrice;
    @FXML
    private Button DeleteButton;
    @FXML
    private Button SaveButton;
    @FXML
    private Button CancelButton;
    @FXML
    private Button SearchButton;
    @FXML
    private TextField SearchField;

    private int index;
    private Product tempProduct;
    private Inventory data;
    private ObservableList<Part> parts = FXCollections.observableArrayList();

    ;
    
    

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ObservableList<Part> getParts() {
        return parts;
    }

    public void setParts(ObservableList<Part> parts) {
        this.parts = parts;
    }

    public Inventory getData() {
        return data;
    }

    public void setData(Inventory data) {
        this.data = data;
        AddPartTable.setItems(data.getAllParts());

    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up columns for Add Part Table
        AddPartID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        AddPartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        AddPartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        AddPartInv.setCellValueFactory(new PropertyValueFactory<>("Stock"));

        //set up columns for Delete Part Table
        DeletePartID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        DeletePartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        DeletePartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        DeletePartInv.setCellValueFactory(new PropertyValueFactory<>("Stock"));

    }

    @FXML
    private void addButtonHandler(ActionEvent event) {
        Part selectedPart = AddPartTable.getSelectionModel().getSelectedItem();
        DeletePartTable.getItems().add(selectedPart);

    }

    @FXML
    private void deleteButtonHandler(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Deleting!");
        alert.setContentText("Are you sure you wish to delete ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            DeletePartTable.getItems().remove(
                    DeletePartTable.getSelectionModel().getSelectedItem());

        }
    }

    @FXML
    private void saveButtonHandler(ActionEvent event) {


        if (productValidation(tempProduct, GetProductName, GetProductInv, GetProductPrice, GetProductMin, GetProductMax)) {
            
            parts.addAll(DeletePartTable.getItems());
            if (parts.isEmpty()) {
                numberAlert("Product must contain at least one part");
                return;
            }
            double sum = 0;
            for (Part p : parts) {

                double price = p.getPrice();
                sum += price;

            }
            if (tempProduct.getPrice() < sum) {
                numberAlert("Product price of: " + tempProduct.getPrice() + " is less than cost of it's parts: " + sum);
                return;
            }
            tempProduct.setAssociatedParts(parts);
       
            data.getAllProducts().remove(index);
            data.modifyProduct(tempProduct);

            SaveButton.getScene().getWindow().hide();

        }

    }

    @FXML
    private void cancelButtonHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirming!");
        alert.setContentText("Are you sure you wish to cancel?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            CancelButton.getScene().getWindow().hide();
        }

    }

    @FXML
    private void searchButtonHandler(ActionEvent event) {
        Part search = data.lookupPart(SearchField.getText());
        if (search != null) {
            AddPartTable.scrollTo(search);
            AddPartTable.getSelectionModel().select(search);

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Error");
            alert.setContentText("Part: " + SearchField.getText() + " not found");

            alert.showAndWait();
        }
    }

    void initData(Product product) {
        this.tempProduct = product;
        productID.setText(Integer.toString(product.getId()));
        GetProductName.setText(product.getName());
        GetProductInv.setText(Integer.toString(product.getStock()));
        GetProductPrice.setText(Double.toString(product.getPrice()));
        GetProductMax.setText(Integer.toString(product.getMax()));
        GetProductMin.setText(Integer.toString(product.getMin()));
        DeletePartTable.setItems(product.getAssociatedParts());
    }

}
