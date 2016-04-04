package org.omilab.robot.mind;
import org.omilab.robot.body.Body;
import org.omilab.robot.interfaces.mindbody.*;

public class Mind implements MindBodyInterface {
	private ProximityModelMind ProximityModelMind;
	private MoveModelMind MoveModelMind;
	private VisionModelMind VisionModelMind;
	private AudioModelMind AudioModelMind;
	private ExpressionModelMind ExpressionModelMind;
	private Body BodyRepresentation;
	
	public Mind() {
		ProximityModelMind = new ProximityModelMind();
		VisionModelMind = new VisionModelMind();
		MoveModelMind = new MoveModelMind();
		ExpressionModelMind = new ExpressionModelMind();
	}

	public void createBodyRepresentation(Body Body) {
		BodyRepresentation = Body;
	}

	@Override
	public void desymbolizeAudioModel() {
		BodyRepresentation.desymbolizeVisionModel();	
	}

	@Override
	public void desymbolizeExpressionModel() {
		BodyRepresentation.desymbolizeExpressionModel();
	}
	
	@Override
	public void desymbolizeMoveModel() {
		BodyRepresentation.desymbolizeMoveModel();
	}
	
	@Override
	public void desymbolizeProximityModel() {
		BodyRepresentation.desymbolizeProximityModel();
	}
	
	@Override
	public void desymbolizeVisionModel() {
		BodyRepresentation.desymbolizeVisionModel();
	}

	public void embody() {
		BodyRepresentation.setMind(this);
	}

	public AudioModelMind getAudioModelMind() {
		return AudioModelMind;
	}

	public ExpressionModelMind getExpressionModelMind() {
		return ExpressionModelMind;
	}
	
	public MoveModelMind getMoveModelMind() {
		return MoveModelMind;
	}
	
	public ProximityModelMind getProximityModelMind() {
		return ProximityModelMind;
	}

	public VisionModelMind getVisionModelMind() {
		return VisionModelMind;
	}
	
	public void startIntelligence() {
		desymbolizeProximityModel();
		symbolizeProximityModel();
		
		//Express Forward Movement
		ExpressionModelMind.setDisplay(ExpressionModelMind.DISPLAYFORWARD);
		ExpressionModelMind.setDisplayColor(ExpressionModelMind.DISPLAYGREEN);
		ExpressionModelMind.setFlippedDisplay(true);
		ExpressionModelMind.setSetDisplay(true);
		desymbolizeExpressionModel();
		
		//Move Forward
		MoveModelMind.setDirection(EnumMoveModelDirection.WD4FORWARD);
		MoveModelMind.setSpeed(255);
		desymbolizeMoveModel();
		
		//Get Sensor Data
		ProximityModelMind.setActiveRgb(ProximityModelMind.RGBFRONT);
		desymbolizeProximityModel();
		symbolizeProximityModel();
		while (ProximityModelMind.hasLeftOuterReflection() && ProximityModelMind.hasLeftInnerReflection() && ProximityModelMind.hasRightOuterReflection() && ProximityModelMind.hasRightInnerReflection()) {
			if (ProximityModelMind.getRgb()[0] < 5 || ProximityModelMind.getRgb()[1] < 5 || ProximityModelMind.getRgb()[2] < 5) {
				//Get Vision Data
				VisionModelMind.setStreaming(false);
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
		MoveModelMind.setDirection(EnumMoveModelDirection.WD4STOP);
		desymbolizeMoveModel();
		
		//Express Stop
		ExpressionModelMind.setDisplay(ExpressionModelMind.DISPLAYSTOP);
		ExpressionModelMind.setDisplayColor(ExpressionModelMind.DISPLAYRED);
		ExpressionModelMind.setSetDisplay(true);
		desymbolizeExpressionModel();
	}

	@Override
	public void symbolizeAudioModel() {
		BodyRepresentation.symbolizeAudioModel();
		AudioModelMind.setStreaming(BodyRepresentation.getAudioModelBody().isStreaming());
		if (!AudioModelMind.isStreaming())
			AudioModelMind.setPathToWavFile(BodyRepresentation.getAudioModelBody().getPathToWavFile());	
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
	public void symbolizeProximityModel() {
		BodyRepresentation.symbolizeProximityModel();
		
		ProximityModelMind.setLeftInnerReflection(BodyRepresentation.getProximityModelBody().hasLeftInnerReflection());
		ProximityModelMind.setLeftOuterReflection(BodyRepresentation.getProximityModelBody().hasLeftOuterReflection());
		ProximityModelMind.setRightInnerReflection(BodyRepresentation.getProximityModelBody().hasRightInnerReflection());
		ProximityModelMind.setRightOuterReflection(BodyRepresentation.getProximityModelBody().hasRightOuterReflection());
		
		ProximityModelMind.setLeftInnerReflectionValue(BodyRepresentation.getProximityModelBody().getLeftInnerReflectionValue());
		ProximityModelMind.setLeftOuterReflectionValue(BodyRepresentation.getProximityModelBody().getLeftOuterReflectionValue());
		ProximityModelMind.setRightInnerReflectionValue(BodyRepresentation.getProximityModelBody().getRightInnerReflectionValue());
		ProximityModelMind.setRightOuterReflectionValue(BodyRepresentation.getProximityModelBody().getRightOuterReflectionValue());
		
		short[] s = BodyRepresentation.getProximityModelBody().getRgb();
		ProximityModelMind.setRgb(s[0], s[1], s[2], s[3], s[4], s[5]);
		
		s = BodyRepresentation.getProximityModelBody().getRotGyrAcc();
		ProximityModelMind.setRotGyrAcc(s[0]/1000, s[1]/1000, s[2]/1000, s[3]/1000, s[4]/1000, s[5]/1000, s[6]/1000, s[7]/1000);
		
		ProximityModelMind.setHeading(BodyRepresentation.getProximityModelBody().getHeadingMin(), BodyRepresentation.getProximityModelBody().getHeadingSec());
		ProximityModelMind.setDistance(BodyRepresentation.getProximityModelBody().getDistance());
	}

	@Override
	public void symbolizeVisionModel() {
		BodyRepresentation.symbolizeVisionModel();
		VisionModelMind.setStreaming(BodyRepresentation.getVisionModelBody().isStreaming());
		if (!VisionModelMind.isStreaming())
			VisionModelMind.setPathToJpgFile(BodyRepresentation.getVisionModelBody().getPathToJpgFile());	
	}
	
}
