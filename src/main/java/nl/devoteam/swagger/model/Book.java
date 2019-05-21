package nl.devoteam.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Book
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-02-07T15:01:57.095+01:00")

public class Book   {
  @JsonProperty("book_isbn")
  private String bookIsbn = null;

  @JsonProperty("book_title")
  private String bookTitle = null;

  @JsonProperty("book_description")
  private String bookDescription = null;

  @JsonProperty("book_price")
  private BigDecimal bookPrice = null;

  @JsonProperty("book_inventory")
  private Integer bookInventory = null;

  public Book bookIsbn(String bookIsbn) {
    this.bookIsbn = bookIsbn;
    return this;
  }

   /**
   * Unique identifier (ISBN) representing a specific book.
   * @return bookIsbn
  **/
  @ApiModelProperty(value = "Unique identifier (ISBN) representing a specific book.")


  public String getBookIsbn() {
    return bookIsbn;
  }

  public void setBookIsbn(String bookIsbn) {
    this.bookIsbn = bookIsbn;
  }

  public Book bookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
    return this;
  }

   /**
   * Title of the book.
   * @return bookTitle
  **/
  @ApiModelProperty(value = "Title of the book.")


  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public Book bookDescription(String bookDescription) {
    this.bookDescription = bookDescription;
    return this;
  }

   /**
   * Description of the book.
   * @return bookDescription
  **/
  @ApiModelProperty(value = "Description of the book.")

  public String getBookDescription() {
    return bookDescription;
  }

  public void setBookDescription(String bookDescription) {
    this.bookDescription = bookDescription;
  }

  public Book bookPrice(BigDecimal bookPrice) {
    this.bookPrice = bookPrice;
    return this;
  }

   /**
   * Price of the book.
   * @return bookPrice
  **/
  @ApiModelProperty(value = "Price of the book.")

  @Valid

  public BigDecimal getBookPrice() {
    return bookPrice;
  }

  public void setBookPrice(BigDecimal bookPrice) {
    this.bookPrice = bookPrice;
  }

  public Book bookInventory(Integer bookInventory) {
    this.bookInventory = bookInventory;
    return this;
  }

   /**
   * The inventory for the book.
   * @return bookInventory
  **/
  @ApiModelProperty(value = "The inventory for the book.")


  public Integer getBookInventory() {
    return bookInventory;
  }

  public void setBookInventory(Integer bookInventory) {
    this.bookInventory = bookInventory;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;
    return Objects.equals(this.bookIsbn, book.bookIsbn) &&
        Objects.equals(this.bookTitle, book.bookTitle) &&
        Objects.equals(this.bookDescription, book.bookDescription) &&
        Objects.equals(this.bookPrice, book.bookPrice) &&
        Objects.equals(this.bookInventory, book.bookInventory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bookIsbn, bookTitle, bookDescription, bookPrice, bookInventory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Book {\n");
    
    sb.append("    bookIsbn: ").append(toIndentedString(bookIsbn)).append("\n");
    sb.append("    bookTitle: ").append(toIndentedString(bookTitle)).append("\n");
    sb.append("    bookDescription: ").append(toIndentedString(bookDescription)).append("\n");
    sb.append("    bookPrice: ").append(toIndentedString(bookPrice)).append("\n");
    sb.append("    bookInventory: ").append(toIndentedString(bookInventory)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

