package com.sixdelta.telegrambot01;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendInvoice;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Document;
import org.telegram.telegrambots.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.lang.Math.toIntExact;

public class MyAmazingBot extends TelegramLongPollingBot { 
	
	@Override
	public void onUpdateReceived(Update update) { 
			// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			// Set variables
			String user_first_name = update.getMessage().getChat().getFirstName();
			String user_last_name = update.getMessage().getChat().getLastName();
			String user_username = update.getMessage().getChat().getUserName();
			long user_id = update.getMessage().getChat().getId();
			String message_text = update.getMessage().getText();
			long chat_id = update.getMessage().getChatId();
			log(user_first_name, user_last_name, Long.toString(user_id), message_text);
			if (message_text.equals("/start")) {
				SendMessage message = new SendMessage() // Create a message object object
					.setChatId(chat_id)
					.setText(EmojiParser.parseToUnicode("Santas cangreburgers! :scream: Un cefalópodo congelado!"));
				SendPhoto pht = new SendPhoto()
					.setChatId(chat_id)
					.setPhoto("AgADAQADxKcxG0M4uETcOB2UDBxq0q5kDDAABFjh0aqACVPNL8YAAgI");
				try {
					sendMessage(message);
					sendPhoto(pht);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (message_text.equals("/pic")) {
				SendPhoto msg = new SendPhoto()
					.setChatId(chat_id)
					.setPhoto("AgADAQAD2KcxG_QQuUQhcUrPx-Bje8Ij9y8ABEIJieZy0Ou5Ui8CAAEC")
					.setCaption("A ver, a ver, qué pasó?");
				try {
					sendPhoto(msg); // Call method to send the photo
				} catch (TelegramApiException e) {
					e.printStackTrace();
			        }
			} else if (message_text.equals("/markup")) {
				SendMessage message = new SendMessage() // Create a message object object
					.setChatId(chat_id)
					.setText("Here is your keyboard");
				// Create ReplyKeyboardMarkup object
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				// Create the keyboard (list of keyboard rows)
				List<KeyboardRow> keyboard = new ArrayList<>();
				// Create a keyboard row
				KeyboardRow row = new KeyboardRow();
				// Set each button, you can also use KeyboardButton objects if you need something else than text
				row.add("Row 1 Button 1");
				row.add("Futuro");
				row.add("Row 1 Button 3");
				// Add the first row to the keyboard
				keyboard.add(row);
				// Create another keyboard row
				row = new KeyboardRow();
				// Set each button for the second line
				row.add("Row 2 Button 1");
				row.add("Row 2 Button 2");
				row.add("Row 2 Button 3");
				// Add the second row to the keyboard
				keyboard.add(row);
				// Set the keyboard to the markup
				keyboardMarkup.setKeyboard(keyboard);
				// Add it to the message
				message.setReplyMarkup(keyboardMarkup);
				try {
					sendMessage(message); // Sending our message object to user
				} catch (TelegramApiException e) {
			            e.printStackTrace();
				}
			} else if (message_text.equals("Row 1 Button 1")) {
				SendPhoto msg = new SendPhoto()
					.setChatId(chat_id)
					.setPhoto("AgADAQAD2KcxG_QQuUQhcUrPx-Bje8Ij9y8ABEIJieZy0Ou5Ui8CAAEC")
					.setCaption("A ver, a ver, qué pasó?");
				try {
					sendPhoto(msg); // Call method to send the photo
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (message_text.equals("Row 1 Button 3")) {
				SendMessage message = new SendMessage() // Create a message object object
					.setChatId(chat_id)
					.setText("Presionaste el botón!");
				InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
				List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
				List<InlineKeyboardButton> rowInline = new ArrayList<>();
				rowInline.add(new InlineKeyboardButton()
					.setText("Presiona este botón :D").setCallbackData("update_msg_text"));
				// Set the keyboard to the markup
				rowsInline.add(rowInline);
				// Add it to the message
				markupInline.setKeyboard(rowsInline);
				message.setReplyMarkup(markupInline);
				try {
					execute(message); // Sending our message object to user
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (message_text.equals("Row 2 Button 1")) {
				SendDocument dcm = new SendDocument()
					.setChatId(chat_id)
					.setDocument("CgADAQADLQADIJv4REsX-R6cKYWQAg");
				SendMessage message = new SendMessage() // Create a message object object
					.setChatId(chat_id)
					.setText("Uh-oh...");
				InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
				List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
				List<InlineKeyboardButton> rowInline = new ArrayList<>();
				rowInline.add(new InlineKeyboardButton()
					.setText("Let's wreck!").setCallbackData("wreck"));
				// Set the keyboard to the markup
				rowsInline.add(rowInline);
				// Add it to the message
				markupInline.setKeyboard(rowsInline);
				message.setReplyMarkup(markupInline);
				try {
					sendDocument(dcm);
					execute(message); // Sending our message object to user
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (message_text.equals("/hide")) {
				SendMessage msg = new SendMessage()
					.setChatId(chat_id)
					.setText("Keyboard hidden");
				ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
				msg.setReplyMarkup(keyboardMarkup);
				try {
					sendMessage(msg); // Call method to send the photo
				} catch (TelegramApiException e) {
					e.printStackTrace();
			        }
			} else if (message_text.equals("Hola")) {
				SendDocument dcm = new SendDocument()
					.setChatId(chat_id)
					.setDocument("CgADAQADGgAD9BDBRAq5IyGmVnkwAg");
				SendMessage msg = new SendMessage()
					.setChatId(chat_id)
					.setText(EmojiParser.parseToUnicode("Saludos, primitivo :wave:"));
				try {
					sendDocument(dcm);
					sendMessage(msg); // Call method to send the photo
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if (message_text.equals("Bob esponja eres tu?")) {
				SendMessage msg = new SendMessage()
					.setChatId(chat_id)
					.setText("NO!! SOY ESPONJATRON 🤖  Bienvenido al futuro! :D");
				SendDocument dcm = new SendDocument()
					.setChatId(chat_id)
					.setDocument("CgADAQADGwADpAABuUTYDiCblzwsNwI");
				try {
					sendMessage(msg); // Call method to send the photo
					sendDocument(dcm);
				} catch (TelegramApiException e) {
					e.printStackTrace();
			        }
			    } else if (message_text.equals("Futuro")) {
				SendPhoto pht = new SendPhoto()
					.setChatId(chat_id)
					.setPhoto("AgADAQADIqgxG_QQwUR0nUfyShEw4p4Z9y8ABAmIiG-klfxC-zkCAAEC")
					.setCaption("En el futuro todo es color cromo!");
			        try {
					sendPhoto(pht);
				} catch (TelegramApiException e) {
					e.printStackTrace();
		        	}
			} else if(message_text.equals("/kelpo")){
				LabeledPrice kelpoPrice = new LabeledPrice();
				kelpoPrice.setLabel("1");
				kelpoPrice.setAmount(2500);
				List<LabeledPrice> kelpoPrices = new ArrayList<LabeledPrice>();
				kelpoPrices.add(kelpoPrice);
				SendInvoice invcKelpo = new SendInvoice()
					.setChatId(update.getMessage().getChatId().intValue())
					.setTitle("Kelpo 1 pz")
					.setDescription("Cereal Kelpo, con el que posiblemente te digan: Oye, anoche te vi en televisión!")
					.setPhotoUrl("https://yt3.ggpht.com/-XIGMD9e4pm8/AAAAAAAAAAI/AAAAAAAAAAA/wTNR7Uakb0A/s288-mo-c-c0xffffffff-rj-k-no/photo.jpg")
					.setPayload("/buy")
					.setProviderToken("284685063:TEST:Y2Q5NmMwNjY0NDRh")
					.setStartParameter("pay")
					.setCurrency("MXN")
					.setPrices(kelpoPrices)
					.setNeedName(true)
					.setNeedPhoneNumber(true)
					.setNeedEmail(true);
				try{
					sendInvoice(invcKelpo);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if(message_text.equals("/spatula")){
				LabeledPrice spatulaPrice = new LabeledPrice();
				spatulaPrice.setLabel("1");
				spatulaPrice.setAmount(49999);
				List<LabeledPrice> spatulaPrices = new ArrayList<LabeledPrice>();
				spatulaPrices.add(spatulaPrice);
				SendInvoice invcSpatula = new SendInvoice()
					.setChatId(update.getMessage().getChatId().intValue())
					.setTitle("Espátula Hidrodinámica 1 pz")
					.setDescription("La espátula hidrodinámica con accesorios sintéticos y palanca turbo que seguramente tu jefe cangrejo avaro te pidió.")
					.setPhotoUrl("https://vignette.wikia.nocookie.net/spongebob/images/7/70/Hydrodynamic_spat.jpg")
					.setPayload("/buy")
					.setProviderToken("284685063:TEST:Y2Q5NmMwNjY0NDRh")
					.setStartParameter("pay")
					.setCurrency("MXN")
					.setPrices(spatulaPrices)
					.setNeedName(true)
					.setNeedPhoneNumber(true)
					.setNeedEmail(true);
				try{
					sendInvoice(invcSpatula);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else {
				SendMessage message = new SendMessage() // Create a message object object
					.setChatId(chat_id)
					.setText(message_text);
				try {
					sendMessage(message); // Sending our message object to user
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		} else if (update.hasPreCheckoutQuery()){
			AnswerPreCheckoutQuery compraExitosa = new AnswerPreCheckoutQuery();
			compraExitosa.setPreCheckoutQueryId(update.getPreCheckoutQuery().getId())
				.setOk(true);
			try{
				answerPreCheckoutQuery(compraExitosa);
			} catch (TelegramApiException e) {
					e.printStackTrace();
			}
		} /*else if (!update.hasPreCheckoutQuery()) {
			AnswerPreCheckoutQuery compraNoExitosa = new AnswerPreCheckoutQuery();
			compraNoExitosa.setPreCheckoutQueryId(update.getPreCheckoutQuery().getId())
				.setOk(false)
				.setErrorMessage("No Patricio, la mayonesa no es un instrumento (la compra no se ha podido completar).");
			try{
				answerPreCheckoutQuery(compraNoExitosa);
			} catch (TelegramApiException e) {
					e.printStackTrace();
			}
		} */else if (update.hasCallbackQuery()) {
			// Set variables
			String call_data = update.getCallbackQuery().getData();
			long message_id = update.getCallbackQuery().getMessage().getMessageId();
			long chat_id = update.getCallbackQuery().getMessage().getChatId();
			if (call_data.equals("update_msg_text")) {
				String answer = "Cual editado papu";
				EditMessageText new_message = new EditMessageText()
					.setChatId(chat_id)
					.setMessageId(toIntExact(message_id))
					.setText(answer);
				try {
					execute(new_message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			} else if(call_data.equals("wreck")){
				String answer = "🤖sdfsdfsadfasdfasdfsadfsadfsafsdafsdafsafasdfsadf🤖🤖🤖🤖";
				SendMessage new_msg = new SendMessage()
					.setChatId(chat_id)
					.setText(answer);
				try {
					execute(new_msg);
				} catch (TelegramApiException e) {
					e.printStackTrace();
			        }
				
			}
		} else if (update.hasMessage() && update.getMessage().hasPhoto()) {
			// Message contains photo
			// Set variables
			long chat_id = update.getMessage().getChatId();
			List<PhotoSize> photos = update.getMessage().getPhoto();
			String f_id = photos.stream()
				.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
				.findFirst()
				.orElse(null).getFileId();
			int f_width = photos.stream()
				.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
				.findFirst()
				.orElse(null).getWidth();
			int f_height = photos.stream()
				.sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
				.findFirst()
				.orElse(null).getHeight();
			String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
			SendPhoto msg = new SendPhoto()
				.setChatId(chat_id)
				.setPhoto(f_id)
				.setCaption(caption);
			try {
			    	sendPhoto(msg); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		} else if (update.hasMessage() && update.getMessage().hasDocument()) {
			// Message contains photo
			// Set variables
			long chat_id = update.getMessage().getChatId();
			Document doc = update.getMessage().getDocument();
			String caption = "file_id: " + doc.getFileId() + "\nname: " + doc.getFileName();
			SendDocument dcm = new SendDocument()
				.setChatId(chat_id)
				.setDocument(doc.getFileId())
				.setCaption(caption);
			try {
			    	sendDocument(dcm); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	} 
	
	@Override public String getBotUsername() { 
		return "esponjatronbot";
	}
	
	@Override public String getBotToken() { 
		return "554030619:AAGXfdE5VK3wp5m-9Nmm9nuXN3KUfZNyZb0";
	}

	private void log(String first_name, String last_name, String user_id, String txt) {
		System.out.println("\n ----------------------------");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
	}

}
