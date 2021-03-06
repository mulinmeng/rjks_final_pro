package com.example.zhangwenqiang.rjks_final_pro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class Player {
//	boolean isdizhu = false;

	int[] cards;

	boolean[] cardsFlag;

	int playerId;


	int currentId;


	int currentCircle;


	int top, left;


	Desk desk;


	CardsHolder latestCards;


	CardsHolder cardsOnDesktop;

	// Context
	Context context;

	int paintDirection = CardsType.direction_Vertical;
	Bitmap cardImage;

	private Player last;
	private Player next;

	public Player(int[] cards, int left, int top, int paintDir, int id, Desk desk, Context context) {
		this.desk = desk;
		this.playerId = id;
		this.cards = cards;
		this.context = context;
		cardsFlag = new boolean[cards.length];
		this.setLeftAndTop(left, top);
		this.paintDirection = paintDir;
	}

	public void setLeftAndTop(int left, int top) {
		this.left = left;
		this.top = top;
	}


	public void setLastAndNext(Player last, Player next) {
		this.last = last;
		this.next = next;
	}


	public void paint(Canvas canvas) {
		System.out.println("id:" + playerId);
		Rect src = new Rect();
		Rect des = new Rect();

		int row;
		int col;


		if (paintDirection == CardsType.direction_Vertical) {
			Paint paint = new Paint();
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(1);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			Bitmap backImage = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.card_bg);

			src.set(0, 0, backImage.getWidth(), backImage.getHeight());
			des.set((int) (left * MainActivity.SCALE_HORIAONTAL),
					(int) (top * MainActivity.SCALE_VERTICAL),
					(int) ((left + 40) * MainActivity.SCALE_HORIAONTAL),
					(int) ((top + 60) * MainActivity.SCALE_VERTICAL));
			RectF rectF = new RectF(des);
			canvas.drawRoundRect(rectF, 5, 5, paint);
			canvas.drawBitmap(backImage, src, des, paint);


			paint.setStyle(Style.FILL);
			paint.setColor(Color.WHITE);
			paint.setTextSize((int) (20 * MainActivity.SCALE_HORIAONTAL));
			canvas.drawText("" + cards.length, (int) (left * MainActivity.SCALE_HORIAONTAL),
					(int) ((top + 80) * MainActivity.SCALE_VERTICAL), paint);

		}
		else {
			Paint paint = new Paint();
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(1);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			Log.d("test",Integer.toString(cards.length));
			for (int i = 0; i < cards.length; i++) {
				row = CardsManager.getImageRow(cards[i]);
				col = CardsManager.getImageCol(cards[i]);
				Log.d("ttttt",String.valueOf(row)+"  "+ String.valueOf(col));
                cardImage = BitmapFactory.decodeResource(context.getResources(),
                        CardImage.cardImages[row][col]);
				int select = 0;
				if (cardsFlag[i]) {
					select = 10;
				}
				src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
				des.set((int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
						(int) ((top - select) * MainActivity.SCALE_VERTICAL),
						(int) ((left + 40 + i * 20) * MainActivity.SCALE_HORIAONTAL), (int) ((top
								- select + 60) * MainActivity.SCALE_VERTICAL));
				RectF rectF = new RectF(des);
				canvas.drawRoundRect(rectF, 5, 5, paint);
				canvas.drawBitmap(cardImage, src, des, paint);

			}
		}

	}
	public void paintResultCards(Canvas canvas) {
		// TODO Auto-generated method stub
		Rect src = new Rect();
		Rect des = new Rect();
		int row;
		int col;

		for (int i = 0; i < cards.length; i++) {
			row = CardsManager.getImageRow(cards[i]);
			col = CardsManager.getImageCol(cards[i]);
			cardImage = BitmapFactory.decodeResource(context.getResources(),
					CardImage.cardImages[row][col]);
			Paint paint = new Paint();
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(1);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			if (paintDirection == CardsType.direction_Vertical) {
				src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
				des.set((int) (left * MainActivity.SCALE_HORIAONTAL),
						(int) ((top - 40 + i * 15) * MainActivity.SCALE_VERTICAL),
						(int) ((left + 40) * MainActivity.SCALE_HORIAONTAL),
						(int) ((top + 20 + i * 15) * MainActivity.SCALE_VERTICAL));
				RectF rectF = new RectF(des);
				canvas.drawRoundRect(rectF, 5, 5, paint);
				canvas.drawBitmap(cardImage, src, des, paint);

			}
			else {
				src.set(0, 0, cardImage.getWidth(), cardImage.getHeight());
				des.set((int) ((left + 40 + i * 20) * MainActivity.SCALE_HORIAONTAL),
						(int) (top * MainActivity.SCALE_VERTICAL),
						(int) ((left + 80 + i * 20) * MainActivity.SCALE_HORIAONTAL),
						(int) ((top + 60) * MainActivity.SCALE_VERTICAL));
				RectF rectF = new RectF(des);
				canvas.drawRoundRect(rectF, 5, 5, paint);
				canvas.drawBitmap(cardImage, src, des, paint);

			}
		}
	}

	public CardsHolder chupaiAI(CardsHolder card) {
		int[] pokeWanted = null;

		if (card == null) {

			pokeWanted = CardsManager.outCardByItsself(cards, last, next);
		}
		else {

			pokeWanted = CardsManager.findTheRightCard(card, cards, last, next);
		}

		if (pokeWanted == null) {
			return null;
		}

		for (int i = 0; i < pokeWanted.length; i++) {
			for (int j = 0; j < cards.length; j++) {
				if (cards[j] == pokeWanted[i]) {
					cards[j] = -1;
					break;
				}
			}
		}
		int[] newpokes = new int[0];
		if (cards.length - pokeWanted.length > 0) {
			newpokes = new int[cards.length - pokeWanted.length];
		}
		int j = 0;
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] != -1) {
				newpokes[j] = cards[i];
				j++;
			}
		}
		this.cards = newpokes;
		CardsHolder thiscard = new CardsHolder(pokeWanted, playerId, context);

		Desk.cardsOnDesktop = thiscard;
		this.latestCards = thiscard;
		return thiscard;
	}


	@SuppressLint("ShowToast")
	public CardsHolder chupai(CardsHolder card) {
		int count = 0;
		for (int i = 0; i < cards.length; i++) {
			if (cardsFlag[i]) {
				count++;
				System.out.println("出牌：" + String.valueOf(CardsManager.getCardNumber(cards[i])));
			}
		}
		int[] chupaiPokes = new int[count];
		int j = 0;
		for (int i = 0; i < cards.length; i++) {
			if (cardsFlag[i]) {
				chupaiPokes[j] = cards[i];
				j++;
			}
		}
		int cardType = CardsManager.getType(chupaiPokes);
		System.out.println("cardType:" + cardType);
		if (cardType == CardsType.error) {

			if (chupaiPokes.length != 0) {
				MainActivity.handler.sendEmptyMessage(MainActivity.WRONG_CARD);
			}
			else {
				MainActivity.handler.sendEmptyMessage(MainActivity.EMPTY_CARD);
			}
			return null;
		}
		CardsHolder newLatestCardsHolder = new CardsHolder(chupaiPokes, playerId, context);
		if (card == null) {
			Desk.cardsOnDesktop = newLatestCardsHolder;
			this.latestCards = newLatestCardsHolder;

			int[] newPokes = new int[cards.length - count];
			int k = 0;
			for (int i = 0; i < cards.length; i++) {
				if (!cardsFlag[i]) {
					newPokes[k] = cards[i];
					k++;
				}

			}
			this.cards = newPokes;
			this.cardsFlag = new boolean[cards.length];
		}
		else {

			if (CardsManager.compare(newLatestCardsHolder, card) == 1) {
				Desk.cardsOnDesktop = newLatestCardsHolder;
				this.latestCards = newLatestCardsHolder;

				int[] newPokes = new int[cards.length - count];
				int ni = 0;
				for (int i = 0; i < cards.length; i++) {
					if (!cardsFlag[i]) {
						newPokes[ni] = cards[i];
						ni++;
					}
				}
				this.cards = newPokes;
				this.cardsFlag = new boolean[cards.length];
			}
			if (CardsManager.compare(newLatestCardsHolder, card) == 0) {
				MainActivity.handler.sendEmptyMessage(MainActivity.SMALL_CARD);
				return null;
			}
			if (CardsManager.compare(newLatestCardsHolder, card) == -1) {
				MainActivity.handler.sendEmptyMessage(MainActivity.WRONG_CARD);
				return null;
			}
		}
		return newLatestCardsHolder;
	}


	public void onTuch(int x, int y) {

		for (int i = 0; i < cards.length; i++) {
			if (i != cards.length - 1) {
				if (CardsManager.inRect(x, y,
						(int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
						(int) ((top - (cardsFlag[i] ? 10 : 0)) * MainActivity.SCALE_VERTICAL),
						(int) (20 * MainActivity.SCALE_HORIAONTAL),
						(int) (60 * MainActivity.SCALE_VERTICAL))) {
					cardsFlag[i] = !cardsFlag[i];
					break;
				}
			}
			else {
				if (CardsManager.inRect(x, y,
						(int) ((left + i * 20) * MainActivity.SCALE_HORIAONTAL),
						(int) ((top - (cardsFlag[i] ? 10 : 0)) * MainActivity.SCALE_VERTICAL),
						(int) (40 * MainActivity.SCALE_HORIAONTAL),
						(int) (60 * MainActivity.SCALE_VERTICAL))) {
					cardsFlag[i] = !cardsFlag[i];
					break;
				}
			}

		}
	}

	public void redo() {
		// TODO Auto-generated method stub
		for (int i = 0; i < cardsFlag.length; i++) {
			cardsFlag[i] = false;
		}
	}

}
