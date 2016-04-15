package org.omilab.robot.mind;
import org.omilab.robot.body.Body;
import org.omilab.robot.interfaces.mindbody.*;

import java.io.IOException;

public class Mind implements MindBodyInterface {
	private ProximityModelMind proximityModelMind;
	private MoveModelMind moveModelMind;
	private VisionModelMind visionModelMind;
	private AudioModelMind audioModelMind;
	private ExpressionModelMind expressionModelMind;
	private Body bodyRepresentation;
	
	public Mind() {
		proximityModelMind = new ProximityModelMind();
		visionModelMind = new VisionModelMind();
		moveModelMind = new MoveModelMind();
		expressionModelMind = new ExpressionModelMind();
	}

	public void createBodyRepresentation(Body Body) {
		bodyRepresentation = Body;
	}

	@Override
	public void desymbolizeAudioModel() throws IOException {
		bodyRepresentation.desymbolizeVisionModel();
	}

	@Override
	public void desymbolizeExpressionModel() throws IOException {
		bodyRepresentation.desymbolizeExpressionModel();
	}
	
	@Override
	public void desymbolizeMoveModel() throws IOException {
		bodyRepresentation.desymbolizeMoveModel();
	}
	
	@Override
	public void desymbolizeProximityModel() throws IOException {
		bodyRepresentation.desymbolizeProximityModel();
	}
	
	@Override
	public void desymbolizeVisionModel() throws IOException {
		bodyRepresentation.desymbolizeVisionModel();
	}

	public void embody() {
		bodyRepresentation.setMind(this);
	}

	public AudioModelMind getAudioModelMind() {
		return audioModelMind;
	}

	public ExpressionModelMind getExpressionModelMind() {
		return expressionModelMind;
	}
	
	public MoveModelMind getMoveModelMind() {
		return moveModelMind;
	}
	
	public ProximityModelMind getProximityModelMind() {
		return proximityModelMind;
	}

	public VisionModelMind getVisionModelMind() {
		return visionModelMind;
	}
	
	public void startIntelligence() throws IOException {
		desymbolizeProximityModel();
		symbolizeProximityModel();
		
		//Express Forward Movement
		expressionModelMind.setDisplay(expressionModelMind.DISPLAYFORWARD);
		expressionModelMind.setDisplayColor(expressionModelMind.DISPLAYGREEN);
		expressionModelMind.setFlippedDisplay(true);
		expressionModelMind.setSetDisplay(true);
		desymbolizeExpressionModel();
		
		//Move Forward
		moveModelMind.setDirection(EnumMoveModelDirection.WD4FORWARD);
		moveModelMind.setSpeed(255);
		desymbolizeMoveModel();
		
		//Get Sensor Data
		proximityModelMind.setActiveRgb(proximityModelMind.RGBFRONT);
		desymbolizeProximityModel();
		symbolizeProximityModel();
		while (proximityModelMind.hasLeftOuterReflection() && proximityModelMind.hasLeftInnerReflection() && proximityModelMind.hasRightOuterReflection() && proximityModelMind.hasRightInnerReflection()) {
			if (proximityModelMind.getRgb()[0] < 5 || proximityModelMind.getRgb()[1] < 5 || proximityModelMind.getRgb()[2] < 5) {
				//Get Vision Data
				visionModelMind.setStreaming(false);
				desymbolizeVisionModel();
				try {
					//wait for camera to create a picture
					Thread.sleep(100);
		
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				symbolizeVisionModel();
			}
			try {
				Thread.sleep(1);
	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Get Sensor Data
			desymbolizeProximityModel();
			symbolizeProximityModel();
		}
		
		// Stop
		moveModelMind.setDirection(EnumMoveModelDirection.WD4STOP);
		desymbolizeMoveModel();
		
		//Express Stop
		expressionModelMind.setDisplay(expressionModelMind.DISPLAYSTOP);
		expressionModelMind.setDisplayColor(expressionModelMind.DISPLAYRED);
		expressionModelMind.setSetDisplay(true);
		desymbolizeExpressionModel();
	}

	@Override
	public void symbolizeAudioModel() throws IOException {
		bodyRepresentation.symbolizeAudioModel();
		audioModelMind.setStreaming(bodyRepresentation.getAudioModelBody().isStreaming());
		if (!audioModelMind.isStreaming())
			audioModelMind.setPathToWavFile(bodyRepresentation.getAudioModelBody().getPathToWavFile());
	}

	@Override
	public void symbolizeExpressionModel() {
		// TODO Here we would detect noise	
	}

	@Override
	public void symbolizeMoveModel() {
		// TODO Here we would detect our own movement
	}

	@Override
	public void symbolizeProximityModel() throws IOException {
		bodyRepresentation.symbolizeProximityModel();
		
		proximityModelMind.setLeftInnerReflection(bodyRepresentation.getProximityModelBody().hasLeftInnerReflection());
		proximityModelMind.setLeftOuterReflection(bodyRepresentation.getProximityModelBody().hasLeftOuterReflection());
		proximityModelMind.setRightInnerReflection(bodyRepresentation.getProximityModelBody().hasRightInnerReflection());
		proximityModelMind.setRightOuterReflection(bodyRepresentation.getProximityModelBody().hasRightOuterReflection());
		
		proximityModelMind.setLeftInnerReflectionValue(bodyRepresentation.getProximityModelBody().getLeftInnerReflectionValue());
		proximityModelMind.setLeftOuterReflectionValue(bodyRepresentation.getProximityModelBody().getLeftOuterReflectionValue());
		proximityModelMind.setRightInnerReflectionValue(bodyRepresentation.getProximityModelBody().getRightInnerReflectionValue());
		proximityModelMind.setRightOuterReflectionValue(bodyRepresentation.getProximityModelBody().getRightOuterReflectionValue());
		
		short[] s = bodyRepresentation.getProximityModelBody().getRgb();
		proximityModelMind.setRgb(s[0], s[1], s[2], s[3], s[4], s[5]);
		
		s = bodyRepresentation.getProximityModelBody().getRotGyrAcc();
		proximityModelMind.setRotGyrAcc(s[0]/1000, s[1]/1000, s[2]/1000, s[3]/1000, s[4]/1000, s[5]/1000, s[6]/1000, s[7]/1000);
		
		proximityModelMind.setHeading(bodyRepresentation.getProximityModelBody().getHeadingMin(), bodyRepresentation.getProximityModelBody().getHeadingSec());
		proximityModelMind.setDistance(bodyRepresentation.getProximityModelBody().getDistance());
	}

	@Override
	public void symbolizeVisionModel() throws IOException {
		bodyRepresentation.symbolizeVisionModel();
		visionModelMind.setStreaming(bodyRepresentation.getVisionModelBody().isStreaming());
		if (!visionModelMind.isStreaming())
			visionModelMind.setPathToJpgFile(bodyRepresentation.getVisionModelBody().getPathToJpgFile());
	}
	
}
