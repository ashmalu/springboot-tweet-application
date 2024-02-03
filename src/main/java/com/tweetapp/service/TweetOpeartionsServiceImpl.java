package com.tweetapp.service;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.model.TweetDetails;
import com.tweetapp.model.UserDetails;
import com.tweetapp.repository.TweetDetailsRepository;
import com.tweetapp.repository.UserDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TweetOpeartionsServiceImpl implements TweetOperationsService {

	@Autowired
	private TweetDetailsRepository tweetDetailsRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	// error messsage
	private final static String ERROR_MSG = "Some error occured while connecting to DB";

	@Override
	public ResponseEntity<?> addTweet(String loginId, String tweet) {
		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if (userDetails.getLoginId().equals(loginId)) {
				TweetDetails tweetDetails = tweetDetailsRepository.findByLoginId(loginId);
				if (tweetDetails == null) {
					TweetDetails t = new TweetDetails();
					t.setLoginId(loginId);
					List<String> lTweet = new ArrayList<>();
					lTweet.add(tweet);
					t.setTweet(lTweet);
					tweetDetailsRepository.save(t);
					ResponseEntity<TweetDetails> response = new ResponseEntity<TweetDetails>(t, HttpStatus.OK);
					return response;
				}
				List<String> lTweet = tweetDetails.getTweet();
				lTweet.add(tweet);
				tweetDetails.setTweet(lTweet);
				tweetDetailsRepository.save(tweetDetails);
				ResponseEntity<TweetDetails> t = new ResponseEntity<TweetDetails>(tweetDetails, HttpStatus.OK);
				return t;
			}
			return (ResponseEntity<?>) ResponseEntity.ok("No User is present with the given login id");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> updateTweet(String loginId, int id, String updatedTweet) {
		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if (userDetails.getLoginId().equals(loginId)) {
				TweetDetails tweetDetails = tweetDetailsRepository.findByLoginId(loginId);
				if (tweetDetails.getLoginId().equals(loginId)) {
					List<String> listTweet = tweetDetails.getTweet();
					listTweet.remove(id - 1);
					listTweet.add(id - 1, updatedTweet);
					tweetDetails.setTweet(listTweet);
					tweetDetailsRepository.save(tweetDetails);
					ResponseEntity<TweetDetails> t = new ResponseEntity<TweetDetails>(tweetDetails, HttpStatus.OK);
					return t;
				}
			}
			return (ResponseEntity<?>) ResponseEntity.ok("No User is present with the given login id");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> deleteTweet(String loginId, int id) {
		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if (userDetails.getLoginId().equals(loginId)) {
				TweetDetails tweetDetails = tweetDetailsRepository.findByLoginId(loginId);
				if (tweetDetails.getLoginId().equals(loginId)) {
					List<String> listTweet = tweetDetails.getTweet();
					List<Map<Integer, Integer>> listMapLikes = tweetDetails.getLikes();
					List<Map<Integer, List<String>>> listMapReply = tweetDetails.getReply();
					if(listTweet.size()==1) {
						listTweet.remove(0);
					}else {
						listTweet.remove(id-1);
					}
					int c = 0;
					int index = 0;
					try {
						if (listMapLikes.size() != 0) {
							for (Map<Integer, Integer> mapLikes : listMapLikes) {
								if (mapLikes.containsKey(id - 1)) {
									mapLikes.remove(id-1);
									listMapLikes.remove(index);
									c = 1;
									break;
								}
								index=index+1;
							}
						}
						
						if(c==0) {
							log.info("No likes present for the this tweet..");
						}else {
							log.info("Likes also deleted..");
						}
					}catch(Exception e) {
						log.info("No likes present for the this tweet..");
					}
					
					try {
						c=0;
						index = 0;
						if (listMapReply.size() != 0) {
							for (Map<Integer, List<String>> mapReply : listMapReply) {
								if (mapReply.containsKey(id - 1)) {
									mapReply.remove(id-1);
									listMapReply.remove(index);
									c = 1;
									break;
								}
								index = index+1;
							}
						}
						
						if(c==0) {
							log.info("No Replies present for the this tweet..");
						}else {
							log.info("Replies also deleted..");
						}
						
					}catch(Exception e) {
						log.info("No Replies present for the this tweet..");
					}
					
					tweetDetails.setTweet(listTweet);
					tweetDetailsRepository.save(tweetDetails);
					ResponseEntity<TweetDetails> t = new ResponseEntity<TweetDetails>(tweetDetails, HttpStatus.OK);
					return t;
				}
			}
			return (ResponseEntity<?>) ResponseEntity.ok("No User is present with the given login id");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> getAllTweets() {
		try {
			List<TweetDetails> listTweetDetails = tweetDetailsRepository.findAll();
			if (listTweetDetails.size() == 0) {
				return (ResponseEntity<?>) ResponseEntity.ok("No tweets yet");
			} else {
				List<List<String>> response = new ArrayList<>();
				for (TweetDetails t : listTweetDetails) {
					response.add(t.getTweet());
				}
				ResponseEntity<List<List<String>>> result = new ResponseEntity<List<List<String>>>(response,
						HttpStatus.OK);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> getAllTweetsByUserName(String loginId) {
		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if (userDetails.getLoginId().equals(loginId)) {
				TweetDetails tweetDetails = tweetDetailsRepository.findByLoginId(loginId);
				if (tweetDetails.getLoginId().equals(loginId)) {
					List<String> listTweet = tweetDetails.getTweet();
					if (listTweet.size() == 0) {
						return (ResponseEntity<?>) ResponseEntity.ok("No tweets yet");
					} else {
						List<String> result = new ArrayList<>();
						for (String s : listTweet) {
							result.add(s);
						}
						ResponseEntity<List<String>> response = new ResponseEntity<List<String>>(result, HttpStatus.OK);
						return response;
					}
				}
			}
			return (ResponseEntity<?>) ResponseEntity.ok("No User is present with the given login id");
		} catch (NullPointerException e) {
			// e.printStackTrace();
			return (ResponseEntity<?>) ResponseEntity.ok("No tweets yet");
		} catch (Exception e) {
			// e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> likeTweet(String loginId, int id) {

		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if (userDetails.getLoginId().equals(loginId)) {
				TweetDetails tweetDetails = tweetDetailsRepository.findByLoginId(loginId);
				if (id <= tweetDetails.getTweet().size()) {
					if (tweetDetails.getLoginId().equals(loginId)) {
						try {
							int v = 0;
							int c = 0;
							int b = 0;
							List<Map<Integer, Integer>> listMapLikes = tweetDetails.getLikes();
							if (listMapLikes.size() != 0) {
								for (Map<Integer, Integer> mapLikes : listMapLikes) {
									c++;
									if (mapLikes.containsKey(id - 1)) {
										// System.out.println("Inside the containsKey method");
										v = mapLikes.get(id - 1);
										v = v + 1;
										b++;
										break;
									}
								}
								// System.out.println("Outside for loop");
								if (b != 0) {
									// System.out.println("inside b++ if");
									Map<Integer, Integer> m = new TreeMap<>();
									m.put(id - 1, v);
									listMapLikes.remove(c - 1);
									listMapLikes.add(c - 1, m);
									tweetDetails.setLikes(listMapLikes);
									tweetDetailsRepository.save(tweetDetails);
									ResponseEntity<Integer> response = new ResponseEntity<Integer>(v, HttpStatus.OK);
									return response;
								} else {
									// System.out.println("inside b++ else");
									Map<Integer, Integer> m = new TreeMap<>();
									m.put(id - 1, 1);
									listMapLikes.add(c - 1, m);
									tweetDetails.setLikes(listMapLikes);
									tweetDetailsRepository.save(tweetDetails);
									ResponseEntity<Integer> response = new ResponseEntity<Integer>(1, HttpStatus.OK);
									return response;
								}

							} else {
								// System.out.println("inside else");
								Map<Integer, Integer> m = new TreeMap<>();
								m.put(id - 1, 1);
								listMapLikes.add(m);
								tweetDetails.setLikes(listMapLikes);
								tweetDetailsRepository.save(tweetDetails);
								ResponseEntity<Integer> response = new ResponseEntity<Integer>(1, HttpStatus.OK);
								return response;
							}
						} catch (NullPointerException e) {
							// System.out.println("Inside null pointer");
							List<Map<Integer, Integer>> listMapLikes = new ArrayList<>();
							Map<Integer, Integer> m = new TreeMap<>();
							m.put(id - 1, 1);
							listMapLikes.add(m);
							tweetDetails.setLikes(listMapLikes);
							tweetDetailsRepository.save(tweetDetails);
							ResponseEntity<Integer> response = new ResponseEntity<Integer>(1, HttpStatus.OK);
							return response;
						}
					}
				} else {
					return (ResponseEntity<?>) ResponseEntity.ok("Not a valid tweet id..");
				}
			}
			return (ResponseEntity<?>) ResponseEntity.ok("No User is present with the given login id");
		} catch (Exception e) {
			// System.out.println("inside outer exception..");
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> replyTweet(String loginId, int id, String reply) {

		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if (userDetails.getLoginId().equals(loginId)) {
				TweetDetails tweetDetails = tweetDetailsRepository.findByLoginId(loginId);
				if (id <= tweetDetails.getTweet().size()) {
					if (tweetDetails.getLoginId().equals(loginId)) {
						try {
							List<String> rep = new ArrayList<>();
							int b = 0;
							int c = 0;
							List<Map<Integer, List<String>>> listMapReply = tweetDetails.getReply();
							if (listMapReply.size() != 0) {
								for (Map<Integer, List<String>> mapReply : listMapReply) {
									c++;
									if (mapReply.containsKey(id - 1)) {
										rep = mapReply.get(id - 1);
										rep.add(reply);
										b++;
										break;
									}
								}
								if (b != 0) {
									Map<Integer, List<String>> m = new TreeMap<>();
									m.put(id - 1, rep);
									listMapReply.remove(c - 1);
									listMapReply.add(c - 1, m);
									tweetDetails.setReply(listMapReply);
									tweetDetailsRepository.save(tweetDetails);
									ResponseEntity<List<String>> response = new ResponseEntity<List<String>>(rep,
											HttpStatus.OK);
									return response;
								} else {
									Map<Integer, List<String>> m = new TreeMap<>();
									List<String> l = new ArrayList<>();
									l.add(reply);
									m.put(id - 1, l);
									listMapReply.add(c - 1, m);
									tweetDetails.setReply(listMapReply);
									tweetDetailsRepository.save(tweetDetails);
									ResponseEntity<List<String>> response = new ResponseEntity<List<String>>(l,
											HttpStatus.OK);
									return response;
								}
							} else {
								Map<Integer, List<String>> m = new TreeMap<>();
								List<String> l = new ArrayList<>();
								l.add(reply);
								m.put(id - 1, l);
								listMapReply.add(m);
								tweetDetails.setReply(listMapReply);
								tweetDetailsRepository.save(tweetDetails);
								ResponseEntity<List<String>> response = new ResponseEntity<List<String>>(l,
										HttpStatus.OK);
								return response;
							}
						} catch (NullPointerException e) {
							List<Map<Integer, List<String>>> listMapReply = new ArrayList<>();
							Map<Integer, List<String>> m = new TreeMap<>();
							List<String> l = new ArrayList<>();
							l.add(reply);
							m.put(id - 1, l);
							listMapReply.add(m);
							tweetDetails.setReply(listMapReply);
							tweetDetailsRepository.save(tweetDetails);
							ResponseEntity<List<String>> response = new ResponseEntity<List<String>>(l, HttpStatus.OK);
							return response;
						}
					}
				} else {
					return (ResponseEntity<?>) ResponseEntity.ok("Not a valid tweet id..");
				}
			}
			return (ResponseEntity<?>) ResponseEntity.ok("No User is present with the given login id");
		} catch (Exception e) {
			// System.out.println("inside outer exception..");
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> viewReplyTweet(String loginId, int id) {

		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if (userDetails.getLoginId().equals(loginId)) {
				TweetDetails tweetDetails = tweetDetailsRepository.findByLoginId(loginId);
				if (id <= tweetDetails.getTweet().size()) {
					if (tweetDetails.getLoginId().equals(loginId)) {
						try {
							List<String> rep = new ArrayList<>();
							int b = 0;
							int c = 0;
							List<Map<Integer, List<String>>> listMapReply = tweetDetails.getReply();
							if (listMapReply.size() != 0) {
								for (Map<Integer, List<String>> mapReply : listMapReply) {
									if (mapReply.containsKey(id - 1)) {
										ResponseEntity<List<String>> response = new ResponseEntity<List<String>>(
												mapReply.get(id - 1), HttpStatus.OK);
										return response;
									}
								}
							} else {
								return (ResponseEntity<?>) ResponseEntity.ok("No reply for the tweet yet");

							}
						} catch (NullPointerException e) {
							return (ResponseEntity<?>) ResponseEntity.ok("No reply for the tweet yet");

						}
					}
				} else {
					return (ResponseEntity<?>) ResponseEntity.ok("Not a valid tweet id..");
				}
			}
			return (ResponseEntity<?>) ResponseEntity.ok("No User is present with the given login id");
		} catch (Exception e) {
			// System.out.println("inside outer exception..");
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

	@Override
	public ResponseEntity<?> viewLikesOnTweet(String loginId, int id) {
		try {
			UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
			if (userDetails.getLoginId().equals(loginId)) {
				TweetDetails tweetDetails = tweetDetailsRepository.findByLoginId(loginId);
				if (id <= tweetDetails.getTweet().size()) {
					if (tweetDetails.getLoginId().equals(loginId)) {
						try {
							List<Map<Integer, Integer>> listMapLikes = tweetDetails.getLikes();
							if (listMapLikes.size() != 0) {
								for (Map<Integer, Integer> mapLikes : listMapLikes) {
									if (mapLikes.containsKey(id - 1)) {
										ResponseEntity<Integer> response = new ResponseEntity<Integer>(
												mapLikes.get(id - 1), HttpStatus.OK);
										return response;
									}
								}

							} else {
								return (ResponseEntity<?>) ResponseEntity.ok("No Likes for the tweet yet..");
							}
						} catch (NullPointerException e) {
							return (ResponseEntity<?>) ResponseEntity.ok("No Likes for the tweet yet..");
						}
					}
				} else {
					return (ResponseEntity<?>) ResponseEntity.ok("Not a valid tweet id..");
				}
			}
			return (ResponseEntity<?>) ResponseEntity.ok("No User is present with the given login id");
		} catch (Exception e) {
			// System.out.println("inside outer exception..");
			e.printStackTrace();
			log.error(ERROR_MSG);
			return (ResponseEntity<?>) ResponseEntity.internalServerError();
		}
	}

}
