LBP.options = {
	// your options here (see list of options below)

	// if video player should start in fullscreen mode; default is "false"
	// autoFullscreen: true,

	// if you want to use native player on iPad; default is "false"
	defaultIPadControls : true,

	// set up default language, en = english, de = german, fr = french, ...
	defaultLanguage : "en",

	// change to browser language if available
	setToBrowserLanguage : true,

	// video controls bar elements;
	// by default all of them available (if present by options and in CSS theme)
	defaultControls : [ "Play", "Pause", "Stop", "Progress", "Timer", "Volume",
			"Playback", "Subtitles", "Sources", "Fullscreen" ],

	// audio controls bar elements;
	// by default all of them available (if present by options and in CSS theme)
	defaultAudioControls : [ "Play", "Pause", "Stop", "Progress", "Timer",
			"Volume" ],

	// show controls bar below video-viewport; default is "false"
	controlsBelow : true,

	// (delayed) hiding of LB player controls; default is "true"
	hideControls : false,

	// if delayed hiding, hide controls bar after x seconds
	hideControlsTimeout : 4,

	// prevent hiding of controls bar if video paused; default is "false"
	hideControlsOnPause : true,

	// if media element should be paused and unfocused (CSS) on focus lost
	pauseOnFocusLost : false,

	// prevent playing more than one media element (player) at same time
	pauseOnPlayerSwitch : true,

	// focus first (video) player on initialization; default is "true"
	focusFirstOnInit : false,

	// if media element buffering should be stopped/restarted on focus
	// lost/re-focused
	// overwrites pauseOnPlayerSwitch to "true"
	// stores playback-status (playing/paused) to come back
	handleBufferingOnFocusLost : true, // (Experimental)

	// if poster-image should reappear once video ended; default is "true"
	posterRestore : false,

	// set default volume; default is "6"
	defaultVolume : 3,

	// set how many volume rates; default is "8"
	volumeRates : 10,

	// hide subtitles element from controls bar; default is "true" (shown)
	// showSubtitles: false,
	showSubtitles : true,

	// set default language for subtitles; can differ from "defaultLanguage"
	defaultSubtitleLanguage : "en",

	// show element in controls bar to change between source resolutions;
	// default is "false"
	showSources : true,

	// show playbackrate element in controls bar to change between
	// "playbackRates"
	// only available if supported by browser (if set to "true"); default is
	// "false"
	showPlaybackRates : true,

	// if playbackrates should be extended; by default following are available
	playbackRates : [ 0.25, 0.5, 1, 2 ],

	// default timer format,
	// could be "PASSED_DURATION" (default), "PASSED_REMAINING",
	// "PASSED_HOVER_REMAINING"
	defaultTimerFormat : "PASSED_DURATION",

	// set up seek-skip in seconds; to jump back or forward x seconds
	seekSkipSec : 3,

	// set up seek-skip in percent; to jump back or forward x percent
	seekSkipPerc : 10,

	// if you want to show your users an embed-code item within the player;
	// default is "false"
	showEmbedCode : true, // (Experimental)

	// logo path/url; set up position with CSS
	logo : "",

	// use preloading spinner or not; by default is "true"
	useSpinner : true,

	// use to adapt spinner circles to your needs (CSS)
	useSpinnerCircles : 7,

	// trigger events of HTML5 media elements
	// eg.: loadstart, progress, suspend, abort, error, emptied, stalled;
	// play, pause; loadedmetadata, loadeddata, waiting, playing, canplay,
	// canplaythrough;
	// seeking, seeked, timeupdate, ended; ratechange, durationchange,
	// volumechange
	triggerHTML5Events : [ "play", "pause" ]
	
	
	
	
	/*** Google Analytics Tracker Extension ***/
	/*// do: define Google Analytics Tracker extension option(s)
    LBP.gaTracker.options = {
        addJSCode: true, // true if extension should add
                            // Google Analytics async Javascript source code
        profileID: "UA-1234567-8", // profile ID (web property ID, website ID)
                                        // events should be tracked for
        debug: true, // true if tracked events should be written to console
    }
    // do: define category to track for
    LBP.gaTracker.trackCategory = "LBP-Tracking";
    // do: define events to be tracked
    LBP.gaTracker.trackEvents = 
        ["VolumeChange", "RateChange", "Seeking", "Seeked", "Ended", "Play", "Pause"];*/
};






/*document.getElementById("leanback-video-id0_play_control").onclick(alert("incrementerNbVues()"));
document.getElementById("leanback-video-id0_play_control").onclick(alert("incrementerNbVues()"));*/
//TODO